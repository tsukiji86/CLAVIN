## Building a Gazetteer Index From a Custom Source

Version 0.4.0 allows the creation of custom CLAVIN indexes by extending `IndexBuilder`.  The process is really straight forward, and offers users a lot of flexibility.  

The `IndexBuilder` is going to do a lot for you.  It&apos;s going to keep track of progress, handle the Lucene indexing details, handle the schema, handle command line argument parsing, etc.  It will even print a nice banner with Cliff Clavin&apos;s face on it.  What else could you want?

To create a custom index, follow these steps:

### Step 1 - Extend `IndexBuilder`

Create a new class, name it whatever you want and have it extend `com.berico.clavin.resolver.impl.lucene.IndexBuilder`.

```
public class CustomIndexBuilder extends IndexBuilder {}
```

### Step 2 - Call the `IndexBuilder`&apos;s constructor.

A lot of the work happens in the `IndexBuilder` constructor, so it needs to be called with the command line input.

```
public CustomIndexBuilder(String[] args) throws Exception {
  super(args);
}
```

### Step 3 - Implement the `main()` entry point.

You can&apos;t inherit a static method, so you will have to declare the main method in your class.  In the main method, simply instantiate your object passing the command line args.

```
public static void main(String[] args) throws Exception {
  new CustomIndexBuilder(args);
}
```

### Step 4 - Provide a Description for the Command Line.

Provide a simple description of what your implementation of `IndexBuilder` does.

```
@Override
protected String getDescription() {  
  return "There's only one reliable place to find Cliff.";
}
```

### Step 5 - (Optionally) Extend the command line parser.

We use [Argparse4J](http://argparse4j.sourceforge.net/) to parse the command line arguments.  At this point, the `IndexBuilder` has already extended it adding the "index" variable (this is where the index will be created).  You are now given the option of registering your own arguments for use in configuring your `IndexBuilder`.

```
@Override
protected void extend(ArgumentParser parser) {

  parser.addArgument("tagline")
    .metavar("Tagline")
    .type(String.class)
    .required(true)
    .help("Something you want to say about this place.");
  
  parser.addArgument("ssn")
    .metavar("SocialSecurityNumber")
    .type(String.class)
    .required(true)
    .help("SSN of user, you don't need to know why we collect this!");
}
```

### Step 6 - (Optionally) Initialize your `IndexBuilder`.

You will be provide an object that has your command line arguments.  It is poorly named `namespace`.  I like to think of it as `ArgumentBucket`.  Collect your arguments and initialize services or connections to datasources, etc.

```
@Override
protected void initialize(Namespace namespace) {
  
  tagline = namespace.getString("tagline");
  
  String ssn = namespace.getString("ssn");
  
  pl("Can you believe it?  This guy just gave me his SSN: %s", ssn);
}
```

> Note:  `pl(String, Object...)` is a shortcut for `System.out.println(String.format(String, Object...));`.  There&apos;s also a `pl(String)`, which is just a call to `System.out.println(String)`.  We&apos;ve even add a `p()` which does not print to a new line.

### Step 7  - Begin iterating over datasource, adding Places to the index.

The super class has initialized, and is now passing control back to the `CustomIndexBuilder`.  All you need to do is use your configuration to call some datasource and convert that data into `Place` objects.

The context is a simple mechanism to allow you to add `Place`s without needing to know the internals of how the `IndexBuilder` works.  It ever provides a counter, letting you know how many records have been processed.

In the future, if new functionality is exposed, it will be in the context, alleviating you from having to read and understand the workflow of `IndexBuilder`.

```
@Override
protected void begin(BuilderContext context) throws Exception {
  
  Place cheersBoston = new Place();
  
  cheersBoston.setName("Cheers Bar and Grill, Boston");
  
  cheersBoston.setAsciiName("Cheers");
  
  cheersBoston.setAlternateNames(
    Arrays.asList(
      "A place where everbody knows you name",
      "A place where everbody's glad you came"));
  
  cheersBoston.setCenter(new LatLon(42.355927, -71.071137));
  
  cheersBoston.setPrimaryCountryCode(CountryCode.US);
  
  // You see, you can put whatever it is you want here.
  cheersBoston.setContext(tagline);
  
  // A Spot location.
  cheersBoston.setFeatureClass(FeatureClass.S);
  
  // A Restaurant.
  cheersBoston.setFeatureCode(FeatureCode.REST);
  
  // I doubt this place ever has 35 patrons.
  cheersBoston.setPopulation(35);
  
  // Add the place to the context to get indexed.
  context.add(cheersBoston);
  
  // You can get the record count at any time.
  pl("Wrote %s record(s).", context.getTotalProcessed());
}
```

As you can see, we created a `Place` record manually.  You could get your data from virtually anywhere and build an index.

### Step 8 - Cleanup Resources.

Finally, the `IndexBuilder` will give you an opportunity to clean up resources.  This is done regardless of whether an exception has occurred in `begin()` and only after `begin()` has fallen out of scope.

```
@Override
protected void cleanup() throws Exception {
  
  pl("Cleaning up the joint, throwing out the drunks.");
}
```

### Step 9 - Creating a Run Script for the Custom `IndexBuilder`.

To run the custom indexer, we&apos;ll create a little script to launch the process and collect your input from the console.

Call this file something like `build-custom-index.sh` (it has been added to the `scripts` directory of the project as an example).  Replace the `com.berico.clavin.examples.CustomIndexBuilder` with the name of the class you created.

```
#!/usr/bin/env sh

TAGLINE=$1
SSN=$2

# This should be wherever you want the index located.
INDEXDIR="./CustomExampleIndexDirectory"

if [ -d "$INDEXDIR" ]; then
  echo "Deleting existing index."
  rm -R "$INDEXDIR"
fi

mvn clean compile

export MAVEN_OPTS="-Xmx2g"

mvn exec:java  -Dexec.mainClass="com.berico.clavin.examples.CustomIndexBuilder" -Dexec.args="$INDEXDIR $TAGLINE $SSN"
```

### Step 10 - Running the Custom `IndexBuilder`.

Assuming you have reference CLAVIN as a dependency in your Maven project, and you're in the root of that project:

```
sh build-custom-index.sh CustomTagLine 123-45-6789
```

If you want to try this example from the root of the CLAVIN project, execute:

```
sh scripts/build-custom-index.sh CustomTagLine 123-45-6789
```

### That&apos;s it, you&apos;re done!

If you want a better, real-world example, look at the `GeonamesIndexBuilder` in the `com.berico.clavin.resolver.impl.lucene` package.