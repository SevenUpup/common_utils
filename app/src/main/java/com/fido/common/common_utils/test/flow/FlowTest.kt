package com.fido.common.common_utils.test.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.runBlocking

/**
 * @author: FiDo
 * @date: 2024/9/29
 * @des:
 */
fun main() {

    runBlocking {
        val flow1 = flow {
            emit(1)
            kotlinx.coroutines.delay(100)
            emit(3)
        }

        val flow2 = flow {
            emit(2)
            kotlinx.coroutines.delay(50)
            emit(4)
        }

        // 将两个flow合并为一个flow
        //1. merge 并发性/无序性/ 实时性：merge函数能够立即处理任何输入流发出的数据，并将其传递给下游。这使得merge非常适合需要处理实时数据的场景。
        val mergedFlow = merge(flow1, flow2)

        mergedFlow.collect { value ->
            println(value)
        }

//        2. flattenConcat
//        顺序执行：flattenConcat按顺序处理每个嵌套的流。当一个流中的所有元素都被发出后，才会开始处理下一个流。这与merge并行处理不同，flattenConcat保证每个流在上一个流完成后再开始。
//        无并发性：与merge不同，flattenConcat是按顺序逐一处理的，不会并发执行每个流。这意味着如果一个流很慢或者有大量元素，后面的流会被阻塞，直到前一个流完成。
//        无序合并：虽然每个子流内部的元素顺序是保留的，但不同子流之间的元素顺序是按顺序依次发出的。
//      ❤ 常见使用场景：flattenConcat适合用于需要按照严格顺序处理多个流的场景。例如，处理多个API请求的响应，其中每个请求的结果必须在下一个请求之前完全处理完毕。
        val flow3 = flow {
            kotlinx.coroutines.delay(20)
            emit(5)
            kotlinx.coroutines.delay(40)
            emit(6)
        }
        val combinedFlow = listOf(flow1, flow2, flow3).asFlow().flattenConcat()
        combinedFlow.collect {
            println("flattenConcat collect=>$it")
        }

        //3.flattenMerge
//        并发处理：flattenMerge 会并发处理所有的流。当有多个嵌套的流时，这些流会同时开始发出数据，而不需要等待其他流完成。
//        无序输出：由于并发处理，flattenMerge 不保证输出数据的顺序。数据的发出顺序取决于每个流内部的处理速度。
//        适用场景：flattenMerge 适合在不关心顺序的情况下，将多个流的数据快速合并处理的场景。例如，当有多个数据源并且需要同时获取数据时，flattenMerge 是一个很好的选择。

//        最终的输出顺序取决于各个流中的 emit 语句执行的速度。
//        ❤使用场景
//        并发数据源：例如，当从多个 API 请求中获取数据，且不关心数据返回的顺序时，可以使用 flattenMerge 将所有请求的结果合并到一个流中。
//        事件处理：当处理来自多个事件源的数据时，如果不关心事件处理的顺序，可以使用 flattenMerge 并发处理这些事件。
        val flowOfFlows = flowOf(flow1, flow2, flow3)
        flowOfFlows.flattenMerge().collect {
            println("flattenMerge => ${it}")
        }

        //3.flatMapConcat
//        flatMapConcat 是一个用于处理嵌套流（Flow of Flow）的操作符。它可以将一个流中的元素转换为多个流，然后将这些流顺序连接成一个单一的流进行收集和处理，它类似于map + faltConcat的结合体。
//       ❤flatMapConcat 的主要特点
//        顺序连接：
//        flatMapConcat 会顺序地处理每个流 。 当第一个流中的所有元素都被处理完毕后 ， 才会开始处理下一个流 。 这与 flatMapMerge 并发处理不同 ， flatMapConcat 保证每个流的处理是顺序进行的。
//
//        无并发性：
//        在使用 flatMapConcat 时，所有的流都是按顺序依次执行的，不会出现并发执行的情况。这意味着如果一个流执行时间较长，那么下一个流会等待前一个流完成后才开始执行。
//
//        常见使用场景：
//        适用于需要保持严格的顺序性，并且需要将每个元素映射为一个流的场景。例如，一个操作需要按顺序执行多个 API 请求，并且每个请求的执行依赖于前一个请求的结果。
//        适用场景
//        严格顺序处理：当需要按照严格的顺序处理每个元素，并且将每个元素映射为一个流时，flatMapConcat 是非常合适的选择。
//        避免并发：在某些情况下，并发处理可能会导致逻辑复杂度增加或者不符合业务需求，flatMapConcat 通过串行处理流，简化了这些场景的处理。
        val numberFlow = flowOf(1,2,3)
        val resultFlow = numberFlow.flatMapConcat { number->
            flow {
                emit("$number: First")
                kotlinx.coroutines.delay(100)
                emit("$number: Second")
            }
        }
        resultFlow.collect { value ->    //1: First  1: Second    2: First 2: Second 3: First 3: Second
            println("flatMapConcat => $value")
        }


        //4. flatMapMerge
//        相当于map + flatMerge，将一个流中的元素转换为多个流，然后将这些流合并成一个单一的流进行收集和处理。与 flatMapConcat 不同，flatMapMerge 会并发处理每个流，因此它在处理速度和效率上更高，但顺序性无法保证。
//        flatMapMerge 的主要特点
//        并发处理：
//        flatMapMerge 会并发地执行每个流的转换和处理。这意味着所有生成的流会同时启动，数据按最快的流到达顺序合并输出。
//        无序输出：
//        由于并发处理，流中元素的输出顺序并不固定，它们会按照处理速度被合并到最终的流中。因此，输出顺序无法保证与原始流的顺序一致。
//        常见使用场景：
//        适用于需要快速处理多个流，并且不关心数据输出顺序的场景。例如，从多个数据源并行获取数据，并将结果合并到一个流中进行处理。

//        适用场景
//        高效并发处理：当需要并行处理多个流，并希望尽快收集和处理结果时，flatMapMerge 是理想的选择。它能够最大化地利用并发性能，特别是在处理时间不均匀的任务时。
//        不关心顺序：在某些情况下，处理结果的顺序并不重要，或者顺序可以通过其他方式处理，这时使用 flatMapMerge 可以显著提高性能。
        val flows = flowOf(1,2,3)
        val mergeResult = flows.flatMapMerge { number->
            flow {
                emit("$number: First")
                kotlinx.coroutines.delay(100)
                emit("$number: Second")
            }
        }
        mergeResult.collect{  //输出 1: First  2: First 3: First 1: Second 2: Second 3: Second
            println("flatMapMerge => ${it}")
        }

        //5. flatMapLatest
//        flatMapLatest 是一个操作符，用于将一个流中的元素转换为多个流，并只保留最新发出的流，丢弃之前的流。先转化成Flow的Flow再顺序展开的操作符，flatMapConcat 也是顺序展开，它和 flatMapConcat 的区别是在于它的名字里边的Latest。mapLatest, transforLatest, 这两个也是顺序展开。但flatMapLatest并不是在手机和转发Flow的时候卡住上游Flow的生产。而是上游继续生产。并且一旦下一个Flow生产出来，就终止当前Flow的收集。开始收集和转发这个刚生产的Flow，展开只是对多个Flow的合并的一种形式。它是把多个Flow的数据放在一个统一的Flow里发送。
//        flatMapLatest 的主要特点
//        只保留最新的流：
//
//        当新的元素到达时，flatMapLatest 会取消之前生成的流，并启动一个新的流。因此，只有最新的流会继续发出数据，之前的流会被丢弃。
//        这意味着如果在旧的流还没发出所有数据之前就有新的元素到达，旧的流会被取消，其余的数据不会被发出。
//
//        适用于处理快速变化的输入：
//        flatMapLatest 非常适合处理用户输入、搜索建议、实时数据流等场景，在这些场景中，保持对最新数据的响应而不是处理过时的数据非常重要。

//        保持顺序性：
//        尽管会丢弃旧流，flatMapLatest 仍然按照输入流发出的顺序处理元素，但每个元素生成的新流是最新的。

        val mapFlow = flowOf(1,2,3)
        mapFlow.flatMapLatest {
            flow {
                emit("$it: First")
                kotlinx.coroutines.delay(100)
                emit("$it: Second")
            }
        }.collect{   //输出 1: First  2: First 3: First  3: Second
            println("flatMapLatest => ${it}")
        }
//        numbersFlow 依次发出 1, 2, 3 三个整数。flatMapLatest 对每个数字生成一个新的流，该流会发出两个字符串（"First" 和 "Second"）。由于 flatMapLatest 只保留最新的流：

//        当数字 1 发出时，流开始执行，并发出 1: First。
//        数字 2 很快发出，此时之前的流会被取消，新的流发出 2: First。
//        数字 3 发出时，流再次被取消，新的流发出 3: First 和 3: Second。

//        适用场景
//        用户输入处理：比如在搜索框中输入字符时，flatMapLatest 可以确保只处理用户最后输入的字符，取消之前的搜索请求。
//        实时数据流：当只关心最新的数据更新，并希望立即响应时，flatMapLatest 是非常合适的。
//        异步任务切换：当一个异步任务在启动前可能会被新的任务替换时，使用 flatMapLatest 可以确保只执行最新的任务。

//        总结
//        flatMapLatest 是处理快速变化的输入流和需要立即响应最新数据的场景的理想工具。通过只保留最新的流，flatMapLatest 可以有效避免不必要的计算和资源浪费，同时确保应用程序对最新输入保持高效的响应。


        //6. combie
//        combine 的主要特点：
//
//        合并多个流：
//
//        combine 可以将两个或多个 Flow 合并成一个 Flow，并且合并后的 Flow 会在任意一个输入 Flow 发出新值时，使用最新的值进行计算，然后发出新的数据项。
//
//        保留每个流的最新值：
//
//        当某一个 Flow 发出新值时，combine 会将这个新值与其他 Flow 的最新值一起传递给提供的组合函数，从而生成新的值。
//
//        无序性：
//
//        combine 不保证输入 Flow 之间的执行顺序，它只关注任意一个输入 Flow 的最新值，并合并所有流的最新值来产生新结果。
//
//        常见使用场景：
//        combine 适合在需要同时监听多个数据源，并且每个数据源的更新都会影响最终结果的场景。例如，将用户输入与实时数据源结合生成动态搜索结果，或者将多个传感器数据结合在一起生成合成数据。

        val combineFlow1 = flowOf(1,2,3).onEach { delay(100) }
        val combineFlow2 = flowOf("A","B","C").onEach { delay(150) }

        val combineResult = combineFlow1.combine(combineFlow2){ number, letter ->
            "$number$letter"
        }
        combineResult.collect{  //// 输出：1A 2A  2B  3B 3C
            println("combine => ${it}")
        }

//        适用场景
//        实时数据合并：比如在金融应用中，可能需要将股票的价格流和交易量流结合起来生成新的指标流。
//        UI状态管理：当多个独立的状态源（如网络请求结果、用户输入等）需要组合在一起更新 UI 时
//        多传感器数据融合：在物联网或机器人应用中，可能需要将多个传感器的读数合并在一起进行综合判断。

        // 7. zip
//        配对合并：
//        zip 操作符将两个 Flow 的对应元素组合在一起。每当两个流都发出了一个新的元素时，zip 会将这两个元素通过提供的组合函数合并成一个新值并发出。
//        等待配对：
//        zip 会等待两个流都发出新值后才会继续进行下一次组合操作。如果其中一个流的元素发射速度比另一个慢，那么 zip 操作符会等待较慢的那个流发出新值。
//        只处理对齐的元素：
//        只有当两个流都发出元素时，这些元素才会被组合成一个新的值并发出。如果两个流长度不一致，zip 会在较短的流结束时停止发射数据。
//        适用场景：
//        zip 适用于那些需要配对处理两个流的场景，例如将两组相关数据组合在一起，或处理需要等待多个数据源都准备好之后才能进行的操作。
        val zipflow1 = flowOf(1, 2, 3).onEach { delay(100) }
        val zipflow2 = flowOf("A", "B", "C").onEach { delay(200) }

        val zippedFlow = zipflow1.zip(zipflow2) { number, letter ->
            "$number$letter"
        }

        zippedFlow.collect { value ->
            println("zip => ${value}")
        }
//        适用场景
//        数据同步处理：当需要同时处理两个同步的流时，zip 可以将它们的对应元素一一配对处理。例如，将来自两个不同传感器的数据配对处理。
//        联合输入：在表单处理或多数据源输入的场景中，zip 可以将来自不同输入源的数据进行配对处理。
//        对比分析：在某些分析场景中，可能需要将两个相关但独立的数据流结合起来进行对比，zip 操作符非常适合这种情况。


        //8. combineTransform
//        用于将多个 Flow 合并成一个 Flow，并在每次任意一个 Flow 发出新值时，使用自定义的转换逻辑生成新的数据项。与 combine 不同的是，combineTransform 可以在合并数据的过程中执行复杂的操作或异步任务。就和map与transform的区别一样，combineTransform不是把结合完的数据提供出来，而是让使用方自己去发送数据，这样就可以不局限于只发送一次数据了。
//        combineTransform 的主要特点
//
//        灵活的转换逻辑：
//
//        combineTransform 允许在合并流的过程中，使用 emit 函数来发射任意数量的值。这使得可以在合并流的同时，进行一些复杂的转换操作，比如发射多个值或进行异步计算。
//        与 combine 的区别：
//        combine 是在每次任意一个 Flow 发出新值时，直接合并所有流的最新值并发出结果。而 combineTransform 则允许在获取到最新值后执行一些自定义逻辑，甚至可以发射多个值或执行其他异步操作。
//
//        适用场景：
//        combineTransform 适用于那些需要在合并流的过程中进行复杂操作的场景。例如，在合并数据流的同时，还需要进行一些额外的计算或根据条件发射不同的值。
        val combineTransformFlow1 = flowOf(1, 2, 3).onEach { delay(100) }
        val combineTransformFlow2 = flowOf("A", "B", "C").onEach { delay(150) }

        val combineTransformFlow = combineTransformFlow1.combineTransform(combineTransformFlow2) { number, letter ->
            emit("$number$letter")
            delay(50)  // 进行一些额外的操作
            emit("$number-$letter")
        }
        combineTransformFlow.collect { value ->
            println("combineTransform => ${value}")
        }
    // 输出：
//        1A
//        1- A
//        2A
//        2-A
//        2B
//        2- B
//        3B
//        3- B
//        3C
//        3-C
//        在这个示例中，flow1 发出整数，flow2 发出字符串。combineTransform 操作符将它们的最新值结合在一起，并使用 emit 发射了两个不同的值：
//
//        每当 flow1 和 flow2 的最新值被接收到，combineTransform 会先发射第一个组合的值，例如 1A，然后执行一些额外的操作（如延迟），再发射第二个值 1-A。
//        因此，输出中可以看到每个组合产生了两个值。
//
//        适用场景：
//        复杂数据处理：当需要在合并流的过程中进行复杂的转换逻辑时，比如发射多个值或执行异步任务，combineTransform 提供了足够的灵活性。
//        条件性发射：在某些情况下，可能希望根据一些条件动态决定要发射的值，combineTransform 允许在获取到所有最新值后进行任意的处理。

    }

}

