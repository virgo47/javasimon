import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class ReactivePokusy {

	public static void main(String[] args) throws InterruptedException {
		Observable<String> words = Observable.fromIterable(
			Arrays.asList("Something", "new", "I", "should", "learn"));
//		words.subscribe(System.out::println);

		Observable<Long> counter = Observable.interval(1, TimeUnit.SECONDS);
//		counter.subscribe(System.out::println);

//		Observable<String> zipped = words.zipWith(counter, (word, count) -> count + ". " + word);
//		zipped.subscribe(System.out::println);

		System.out.println();
		words.flatMap(word -> Observable.fromArray(word.toLowerCase().split("")))
			.distinct()
			.sorted()
			.zipWith(counter, (word, count) -> count + ". " + word)
			.subscribe(System.out::println);

	}
}
