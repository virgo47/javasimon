# Templating localization with gender and case

In this article we will explore the problem how to localize messages that repeat a lot and only
a small part of the message varies. The variable part will be typically some domain object and
repeating messages are various confirmations, error and similar messages revolving around these
domain objects.

Our code examples will be in Java using standard Java `MessageFormat` and version using
[ICU project](http://site.icu-project.org), [ICU4J](http://userguide.icu-project.org/icufaq/icu4j-faq)
to be precise. While ICU library is rather taxing on disk space (10 MB if you download it using
Maven, as it contains all possible localization data) and also their patterns are more verbose
than the one from `java.text` there is one big advantage. Besides the ICU4C (C version) also
available on the ICU site there are other JavaScript implementations. Maybe not official, but they
refer to ICU and this sounds promising if you need the same message resources both on Java backend
and JavaScript frontend.

Before we go on I'll [point to](https://developer.mozilla.org/en-US/docs/Mozilla/Localization/Localization_content_best_practices)
[various](https://phraseapp.com/blog/posts/localization-10-common-mistakes-in-software-localization-and-how-to-avoid-them/)
[resources](http://www.techrepublic.com/blog/10-things/10-tips-and-best-practices-for-software-localization/)
regarding best practices of localization. Virtually any bigger project where localization matters
has something similar - and among other natural things (like prefer Unicode or never use hardcoded
strings) there is one notoriously repeated warning: *Don't concatenate localized strings.*

Reason for this is very obvious if/when you try any other language than your default because even
similar languages have different structures for some things. Sometimes the number goes first and
then the date, sometimes the other way - things like that. How does this relate to our problem?

## Example sentences

Imagine an application where you can select multiple things in a table and delete them or delete
using some filter. We need message that announces the result to the user. And we also can edit
an object and we want to see that it was successfully saved. This is enough for our needs. Our
domain objects are: Transaction and Client.

### English language

Example sentences with bold showing our noun (domain object) that can change and italics for
other affected words:

* **Transaction** was successfully saved.
* *No* **transactions** *were* deleted. (singular variation possible)
* *One* **transaction** *was* deleted.
* *2* **transactions** *were* deleted. (or more)

Problems to note:

* We need *Transaction* or *transaction* depending on the position in the sentence. *Solution:
Either use the whole phrase per domain object (noun). For insertion into some template format we
need two different keys or a single key with some pre-processing (de)capitalizing the first letter.
This either needs to be supported on the message format level or we can do it in the code after the
whole sentence is completely formatted. We cannot choose/change casing for a single inserted word
because for various languages it can be at different positions in the sentence.*
* We need plural/singular for a noun - this possibly combined with the need for various casing
of the first character. We also need to show the parameter (number), possibly with word versions
for some cases ("No"). Finally, we need to use were/was appropriately. *Solution: some choice
format mechanism, mostly readily available.*

In overall, no problem to add new domain object (nouns) and put them into the sentences somehow.
But this ties us to English rather too much. Let's see some different language to see the whole
problem.


### Slovak language

The same sentences in Slovak (bold for noun and italics for other affected words):

* **Transakcia** *bola* úspešne *uložená*. (The subject noun is in Slovak "nominative" case, and
"bola ... uložená" is affected by the feminine gender of "transakcia". This sentence is needed
only in singular.)
* *Žiadna* **transakcia** *nebola zmazaná*. (Word "transakcia" is in singular "nominative", this
time with lowercase. The rest of the sentence reflects case for "none deleted" with all three
words affected by the feminine gender of "transakcia". Just like in English, plural variation is
possible. Unlike English, word "nebola" means "was not" - sentences with two negatives are normal
in Slovak and these don't cancel out.)
* *Jedna* **transakcia** *bola zmazaná*. (Singular nominative, feminine affecting the whole
sentence, this time with positive "bola zmazaná", that is "was deleted".)
* *2* **transakcie** *boli zmazané*. (For cases 2-4: Plural nominative, feminine.)
* *5* **transakcií** *bolo zmazaných*. (For cases 5 and more: Plural **genitive**, feminine. Here
the number plays the "nominative" role and the whole subject here is roughly like "five (of)
transactions" and instead of "of" Slovak uses genitive case.)

Alternative example for "Transaction was successfully saved", something like "Saving of transaction
was successful":

* Ukladanie **transakcie** bolo úspešné. (Here none of the words are affected by the noun gender,
it wouldn't even be affected by the number. However, the noun itself is not in "nominative" case
anymore, instead it is in "genitive". This cannot be generalized to some "object" case (in addition
to normal "subject" case) as objects in Slovak can be in different cases, mostly "accusative".
This information also cannot be worked with in the code, not even if the code is related to a
single message localization, because this information relates to a single language, here Slovak.
We explore the possibilities with various approaches lower.)

Problems:

* Many words in the template itself are affected by the gender of the noun. *Solution: Localize
complete messages. Using templates we need to obtain the gender information somehow (part of the
key? another key? what about languages where it does not matter at all?) and use it as a parameter
for the formatting mechanism (some choice/select format).*
* We may need to use various cases of the same noun, depending on the message or even a parameter
of the message (like the count of transactions).

### And what about the Client?

You can imagine the messages with "client" in English, just replace the single word. No inflection,
no cases, just respect the number and letter casing depending on the position in the sentence.

Things are different in Slovak though. Let's see the sentence about about Client being saved but
first let's repeat the one about transaction for comparison:

* **Transakcia** *bola* úspešne *uložená*. (Singular, feminine, noun in nominative.)
* **Klient** *bol* úspešne *uložený*. (Singular, but masculine, nominative.)

Rather innocent change in English is a nightmare in Slovak if you want to reuse the structure of
the message somehow. So what are our options?

## Approach 1: whole sentences

### Pure Java solution

Imaginary localization file `Simple.properties`:
```
transaction.saved=Transaction was successfully saved.
transaction.deleted={0,choice,0#No transaction was|1#One transaction was\
  |1<{0} transactions were} deleted.

client.saved=Client was successfully saved.
client.deleted={0,choice,0#No client was|1#One client was\
  |1<{0} clients were} deleted.
```

Last word "deleted" might be included into each sentence too - and this is actually recommended
when your choice already involves significant portion of the sentence anyway.

The same for Slovak in `Simple_sk.properties`:
```
transaction.saved=Transakcia bola úspešne uložená.
transaction.saved.alt=Ukladanie transakcie bolo úspešné.
transaction.deleted={0,choice,0#Žiadna transakcia nebola zmazaná|1#Jedna transakcia bola zmazaná\
  |1<{0} transakcie boli zmazané|4<{0} transakcií bolo zmazaných}.

client.saved=Klient bol úspešne uložený.
client.saved.alt=Ukladanie klienta bolo úspešné.
client.deleted={0,choice,0#Žiadny klient nebol zmazaný|1#Jeden klient bol zmazaný\
  |1<{0} klienti boli zmazaní|4<{0} klientov bolo zmazaných}.
```

And some demo program to print it out:
```java
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class SimpleApproach {
	public static void main(String[] args) {
		showDemo("transaction", Locale.getDefault());
		showDemo("client", Locale.getDefault());
		showDemo("transaction", Locale.forLanguageTag("sk"));
		showDemo("client", Locale.forLanguageTag("sk"));
	}

	private static void showDemo(String domainObject, Locale locale) {
		System.out.println("\nLOCALE: " + locale);
		print(locale, domainObject + ".saved");
		print(locale, domainObject + ".saved.alt");
		for (Integer count : Arrays.asList(0, 1, 2, 4, 5, 99)) {
			print(locale, domainObject + ".deleted", count);
		}
	}

	private static void print(Locale locale, String messageKey, Object... args) {
		String message = format(locale, messageKey, args);
		System.out.println(messageKey + Arrays.toString(args) + ": " + message);
	}

	private static String format(Locale locale, String key, Object... args) {
		ResourceBundle bundle = ResourceBundle.getBundle("Simple", locale);
		try {
			String pattern = bundle.getString(key);
			return new MessageFormat(pattern, locale)
				.format(args);
		} catch (MissingResourceException e) {
			return "";
		}
	}
}
```

### ICU4J solution

I'll use ICU4J `MessageFormat` instead of the one from `java.util`. The usage is actually the
same for both cases, only the `import` statement and loaded resource is different. ICU4J allows
not only positional arguments, but also named ones. For this reason we also changed how the
arguments are provided, because named parameters in a map are much cleaner. But first the
resource files - `SimpleIcu.properties`:

```
transaction.saved=Transaction was successfully saved.
transaction.deleted={count,plural,=0 {No transaction was}\
  one {One transaction was}\
  other {{count} transactions were}} deleted.

client.saved=Client was successfully saved.
client.deleted={count,plural,=0 {No client was}\
  one {One client was}\
  other {{count} clients were}} deleted.
```

And for Slovak - `SimpleIcu_sk.properties`:
```
transaction.saved=Transakcia bola úspešne uložená.
transaction.saved.alt=Ukladanie transakcie bolo úspešné.
transaction.deleted={count,plural,=0 {Žiadna transakcia nebola zmazaná}\
  one {Jedna transakcia bola zmazaná}\
  few {{count} transakcie boli zmazané}\
  other {{count} transakcií bolo zmazaných}}.

client.saved=Klient bol úspešne uložený.
client.saved.alt=Ukladanie klienta bolo úspešné.
client.deleted={count,plural,=0 {Žiadny klient nebol zmazaný}\
  one {Jeden klient bol zmazaný}\
  few {{count} klienti boli zmazaní}\
  other {{count} klientov bolo zmazaných}}.
```

Program listing:
```java
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.ibm.icu.text.MessageFormat;

public class SimpleIcuApproach {
	public static void main(String[] args) {
		showDemo("transaction", Locale.getDefault());
		showDemo("client", Locale.getDefault());
		showDemo("transaction", Locale.forLanguageTag("sk"));
		showDemo("client", Locale.forLanguageTag("sk"));
	}

	private static void showDemo(String domainObject, Locale locale) {
		System.out.println("\nLOCALE: " + locale);
		print(locale, domainObject + ".saved", Collections.emptyMap());
		print(locale, domainObject + ".saved.alt", Collections.emptyMap());
		for (Integer count : Arrays.asList(0, 1, 2, 4, 5, 99)) {
			print(locale, domainObject + ".deleted", Collections.singletonMap("count", count));
		}
	}

	private static void print(Locale locale, String messageKey, Map args) {
		String message = format(locale, messageKey, args);
		System.out.println(messageKey + args + ": " + message);
	}

	private static String format(Locale locale, String key, Map args) {
		ResourceBundle bundle = ResourceBundle.getBundle("SimpleIcu", locale);
		try {
			String pattern = bundle.getString(key);
			return new MessageFormat(pattern, locale)
				.format(args);
		} catch (MissingResourceException e) {
			return "";
		}
	}
}
```

Message output is the same for both cases except for `args` part, but that is part of the
label, not part of the final message. *Please note that the demo listings don't work efficiently
with resource bundles as they call `getBundle` for every formatting. Do it better in production,
or use some other abstraction above it, e.g. Spring.*

### Pros and cons

It's easy! It's easy to read the messages, or at least as easy as it gets. The trouble is that
every time you add a new domain object you need to kinda replicate all the generic messages for it.
Here we have two messages only, but imagine there's ten of them or more. This is not exactly
[DRY](https://en.wikipedia.org/wiki/Don't_repeat_yourself), but in localization world it is quite
safe way how to play it. The trouble is that if you decide to change the generic message you have
to go over many places (consequence of duplication).

Can we do a bit better? Can we template the messages and combine them with some forms of those
domain object names?

## Approach 2: single template and noun nesting

We already foreshadowed all the problems our template solution must overcome. Concatenation is
out of the question - but we're playing it nicely with templates that allows structure of the
sentence being completely different in various languages. But we still have to:

* Treat starting letter of the sentence somehow (when it is a sentence, but let's say we always
know).
* Use a specific inflection form in a template when the list of forms (cases and numbers) is not
known to the code. It must not, of course, as the list may vary depending on a language. For nouns
in English we just need singular/plural, but for Slovak you need nominative/genitive/accusative
in both singular/plural forms - we have even more cases, but the unused ones are not important.
* Template message needs to know the gender of the noun (domain object) as it may affect a lot
of words in it.

Reading the list of problems we need to retrieve some information about the object domain noun
*without our code really understanding it* and pass it somehow to the template. So we don't know
much about the information we need about the noun in advance. Maybe we can narrow it to the
case/number combination, but it actually doesn't matter. We know there are multiple forms of the
noun - but the code doing our templating doesn't know the names of the forms. Only template and
the noun knows - and only in a particular language.

We don't even know what forms for a particular template are needed - the writer or the template
knows. We can only settle on a set of needed forms for the nouns. In case we need a new form, for
instance for some new template, we have to add the new form for all existing nouns. But this
design is still orthogonal. The question is - how to offer all the available forms to the template?

### ICU4J solution

I'll start with ICU4J because it allows those named parameters we already used in the simple
approach. We can just fill the map with all possible forms of the word (and gender or other
required meta-information) and let the template do the job. So how can we get a map of all the
forms? Will we have multiple keys in the bundle with some suffix? I don't like that. Let's do
something more brutal - we put some serialized map into a single key.

We can go for JSON, but as I don't want to import any JSON library, I'll use some special
characters I really don't expect in the names of the domain objects. If you expect to use comma
in the text, use ; and if that's too similar to : for your taste, use |. This simple map format
is actually much better for manual editing.

English resource file `TemplateIcu.properties` is pretty boring:
```
domain.object.transaction=sing:transaction,pl:transactions
domain.object.client=sing:client,pl:clients

object.saved={sing} was successfully saved.
object.deleted={count,plural,=0 {No {sing} was}\
  one {One {sing} was}\
  other {{count} {pl} were}} deleted.
```

We have both domain object names in singular (sing) and plural (pl) forms. Templating for English
is obviously a no-brainer, with only two templates for two objects we are already saving a lot of
characters. How about Slovak property file `TemplateIcu_sk.properties`?
```
domain.object.transaction=gend:fem,nom:transakcia,gen:transakcie,\
  pl:transakcie,plgen:transakcií
domain.object.client=gend:mas,nom:klient,gen:klienta,\
  pl:klienti,plgen:klientov

object.saved={nom} {gend, select, mas {bol úspešne uložený}\
  fem {bola úspešne uložená}\
  other {!!!}}.
object.saved.alt=Ukladanie {gen} bolo úspešné.
object.deleted={gend, select,\
  mas {{count,plural,\
    =0 {Žiadny {nom} nebol zmazaný}\
    one {Jeden {nom} bol zmazaný}\
    few {{count} {pl} boli zmazaní}\
    other {{count} {plgen} bolo zmazaných}}} \
  fem {{count,plural,\
    =0 {Žiadna {nom} nebola zmazaná}\
    one {Jedna {nom} bola zmazaná}\
  	few {{count} {pl} boli zmazané}\
    other {{count} {plgen} bolo zmazaných}}}\
  other {!!!}}.
```

Ok, at this moment it's longer than the original file, but I'd say with the third domain object
we would already save some characters - granted we don't need more forms and genders. Eventually,
for Slovak at least, we would add neuter gender too, but that's about it.

Now the code that runs it:
```java
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.ibm.icu.text.MessageFormat;

public class TemplateIcuApproach {
	public static void main(String[] args) {
		showDemo("transaction", Locale.getDefault());
		showDemo("client", Locale.getDefault());
		showDemo("transaction", Locale.forLanguageTag("sk"));
		showDemo("client", Locale.forLanguageTag("sk"));
	}

	private static void showDemo(String domainObject, Locale locale) {
		System.out.println("\nLOCALE: " + locale);
		print(locale, domainObject, "object.saved", Collections.emptyMap());
		print(locale, domainObject, "object.saved.alt", Collections.emptyMap());
		for (Integer count : Arrays.asList(0, 1, 2, 4, 5, 99)) {
			print(locale, domainObject, "object.deleted", Collections.singletonMap("count", count));
		}
	}

	private static void print(Locale locale, String domainObject, String messageKey, Map args) {
		ResourceBundle bundle = ResourceBundle.getBundle("TemplateIcu", locale);
		Map objectInfo = parseObjectInfo(bundle.getString("domain.object." + domainObject));
		// not generified, sorry; we know that objectInfo is mutable, so we do it this way
		objectInfo.putAll(args);
		String message = format(bundle, locale, messageKey, objectInfo);
		if (sentenceRequiresCapitalization(message, true)) {
			message = Character.toUpperCase(message.charAt(0)) + message.substring(1);
		}
		System.out.println(messageKey + args + ": " + message);
	}

	private static boolean sentenceRequiresCapitalization(String message, boolean isSentence) {
		return isSentence && message != null && !(message.isEmpty())
			&& Character.isLowerCase(message.charAt(0));
	}

	// no sanity checking here, but there should be some
	private static Map parseObjectInfo(String objectInfoString) {
		Map map = new HashMap();
		for (String form : objectInfoString.split(" *, *")) {
			String[] sa = form.split(" *: *");
			map.put(sa[0], sa[1]);
		}
		return map;
	}

	private static String format(ResourceBundle bundle, Locale locale, String key, Map args) {
		try {
			String pattern = bundle.getString(key);
			return new MessageFormat(pattern, locale)
				.format(args);
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		} catch (MissingResourceException e) {
			return "";
		}
	}
}
```

Remember again that we should not call `getBundle` as often as I do here. Take this just as a proof
of concept. :-) Code also performs capitalization of the first letter when the message is sentence
- that's the `true` argument in the call. This information could be embedded into the format too,
but that could be awkward. Best solution would be some custom formatter for an argument, something
like `{nom, capitalizeFirst}`, but I can't find any mechanism to do that with this `MessageFormat`
class. But that is rather minor issue.

Actually, there may be better alternative - just use two separate keys for nominative, that is
"Nom" and "nom" (that is sing/Sing in English resources) with respective casing of the first
letter. We don't need this distinction for other forms, at least not here, not yet. Most of the
cases will hardly appear at the start of the sentence.

We can easily create a tool that checks validity of the keys starting with "domain.object." and
indicate those that don't have expected forms for a particular language. And we can also easily
use this solution in JavaScript - which is very important for us.

### Pure Java solution

It is not completely impossible to do this in Java, but you'd need to use indexes - and that sucks.
If you needed to find all the occurrences of nominative it's easy with ICU's named parameters. Just
search for `{nom}` which is pretty distinct pattern. With Java you'd have to decide fixed indexes
for the forms and then ... put the real arguments as indexes after these? What?! What if I discover
I need to add another form? Will I renumber all arguments (only for that language, mind you) to
make a room for it? And searching? Should I search for `{0}` as nominative? What about cases where
we don't use domain objects in the message and zeroth argument means something completely
different?

Honestly, we will skip this solution altogether. It's not viable.

### Pros and cons

Before I wrote the proof of concept I was not sure if I tried this - but now I'm pretty sure we
will try it! The key was to come up with the answer to the question how to feed the template
message pattern with all possible forms? How to do it without code knowing about languages and
concrete form names? With the map structure serialized in the domain object key this is all easy.

Sure, the generic messages are a bit more messy, but we don't repeat ourselves anymore. It seems
that combo gender-select/plural works for most cases. There are probably even more demanding
messages, but I believe that the deletion message with count shows worthiness of this solution.

## Multiple inserted words?

What if we want to insert multiple nouns? What if the sentence is something like "**Transaction**
for **client** *XY* is above limit"? Here "transaction" and "client" are the domain objects and
"XY" is a concrete name of a client (not a problem, we - hopefully - don't want to inflect that).
If we merge form maps for `domain.object.transaction` and `domain.object.client` one will override
the other. That's not good. What we need is to give these guys some *role*.

Before going on, I have to admit that this example is not a good one, because if only transactions
can break limits and if we always want to show a client there, this would be message with a key
like `transaction.above.limit` and all inserted words in all languages would be there verbatim, no
insertion, only the name of the client would really be an argument. So before we get carried away
but the opportunity to use it everywhere we need to think and prefer NOT to use it when not
necessary. Just because we have our "multi-form" dictionary of domain objects doesn't mean we want
to use it. There is one legitimate case when you might to - when you expect that you change the
word for "transaction" and/or "client". But even if you think it may be actually useful for
American vs British or similar - again, don't. You want different bundles for both language
variants with specified country.

We should not go on with wrong example, right? So how about the sentence "There are some
**transactions** for a **client** *XY* - do you want to delete them as well?" From business point
this still is silly, because we probably don't want to delete anything like this, but at least
we can see that this sentence can be used over and over for various combinations of domain objects.

I'll not implement the whole solution here, let's just shed a bit of light on it. Messages in
English - this time we introduced singular with indefinite article (`singwa`):
```
domain.object.transaction=sing:transaction,singwa:a transaction,pl:transactions
domain.object.client=sing:client,singwa:a client,pl:clients

object.delete.constraint.warning=There are some {slave.pl} for {master.singwa} {name} - do you...?
```

The same in Slovak:
```
domain.object.transaction=gend:fem,nom:transakcia,gen:transakcie,\
  pl:transakcie,plgen:transakcií
domain.object.client=gend:mas,nom:klient,gen:klienta,\
  pl:klienti,plgen:klientov

object.delete.constraint.warning=Pre {master_gen} {name} existujú {slave_pl} - zmažeme...?
```

This time it was easy, no gender, but you can expect it can get more complicated. The point is
we indicated the *role* of the domain object with the prefix, like `master_`. Now how we call
message formatting to work like this? I'll offer a snippet of code, but again, its API can be
groomed, but you'll probably finish this design with your needs in mind anyway:
```java
...
print(loc, "object.delete.constraint.warning",
	Collections.singletonMap("name", "SuperCo."),
	new DomainObject("client", "master"),
	new DomainObject("transaction", "slave"));
...

private static void print(
	Locale locale, String messageKey, Map args, DomainObject... domainObjects)
{
	ResourceBundle bundle = ResourceBundle.getBundle("TemplateIcu", locale);

	// not generified, sorry
	Map finalArgs = new HashMap(args);
	for (DomainObject domainObject : domainObjects) {
		finalArgs.putAll(domainObject.parseObjectInfo(bundle));
	}

	String message = format(bundle, locale, messageKey, finalArgs);
	// sentence capitalization missing, or done using various forms (I like that more and more)
	System.out.println(messageKey + args + ": " + message);
}

// format method like before, parseObjectInfo is embedded into following class:
public class DomainObject {
	private final String domainObject;
	private final String role;

	public DomainObject(String domainObject, String role) {
		this.domainObject = domainObject;
		this.role = role;
	}

	public DomainObject(String domainObject) {
		this.domainObject = domainObject;
		this.role = null;
	}

	// no sanity checking here, but there should be some
	Map parseObjectInfo(ResourceBundle bundle) {
		String objectInfoString = bundle.getString("domain.object." + domainObject);
		Map map = new HashMap();
		for (String form : objectInfoString.split(" *, *")) {
			String[] sa = form.split(" *: *");
			String key = role != null ? role + '_' + sa[0] : sa[0];
			map.put(key, sa[1]);
		}
		return map;
	}
}
```

That's it! Not just 1 insertion anymore, but N - we know only two cardinalities 1 and N, right?

## Your API for translation sucks!

There is no API, these are just demo programs. I'd aim for something much more fluent indeed.
Resource bundle would be somehow available, for instance for current user's locale.
Then the code could go like this:
```java
String message = new LocalizedMessage(resourceBundle, "delete.object")
  .withParam("count", deletedCount)
  .forDomainObject(editor.getDomainObjectName()) // optional role parameter possible
  .format();
```

This code can be in some unified object(s) deleted confirmation dialog that can be reused across
many specific editors. Editor merely needs to provide the name of the domain object (generic one
like "transaction", not the name for the particular instance). I guess this API makes sense and
it's easy to get there.

## Conclusion

Writing this post was part of the exploratory process here, I wasn't sure what the conclusion be.
I now see that the template solution is viable, but it builds on the `MessageFormat` that supports
named parameters. As we already use ICU to align our backend (ICU4J) and frontend (we use
[yahoo/intl-messageformat](https://github.com/yahoo/intl-messageformat)) we can build on that.

I didn't go into performance characteristics, but we're talking about enterprisy software already.
The whole message formatting is not cheap anyway. Unified template is definitely more complex to
parse, at least the gender there is on top of original patterns. We can cache domain object maps,
of course after we change the way we merge them with actual arguments as my solution up there
adds arguments into this map. But otherwise I don't believe it's a big deal. I never compared
ICU with `java.text`, but we need ICU features and don't see any problems yet.

So, yes, templating of often used messages with some snippets changing in them is possible and
makes sense. No it's not concatenation. I believe we're not doing anything internationally illegal
here. :-)