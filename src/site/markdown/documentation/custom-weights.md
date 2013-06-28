## Applying Custom Weights to Resolution

CLAVIN 0.4.0 introduced the concept of weighted resolution (outside of relevancy at the index).  This is especially important for coordinate resolution, where there is little context to determine which `Place` of many should be chosen in a given area (especially one that is exceptionally dense - like New York City).

Implementing custom weights for coordinate resolution in CLAVIN is trivial (we still need to complete the weighting strategy for plain-named locations).  This tutorial will demonstrate exactly how it&apos;s done.

Pretend we had a use case where we wanted to emphasize the selection of Schools above other features.  Perhaps we know that our audience is less concerned with big areas (like cities) and is more focused on what Geonames calls "Spot" features.  To emphasize the selection of schools, we simply need to boost the score of all `ResolvedCoordinate`s that have the `FeatureCode` `SCH` (school).

`ResolvedCoordinateWeigher`s are presented `ResolvedCoordinate` candidates, in addition to the plain-named locations, when a coordinate is extracted from the text.  To influence the overall score of a `ResolvedCoordinate`, you simply need to add your weigher to the set of weighers influencing the selection of the `ResolvedCoordinate`.

### Step 1 - Implement a `ResolvedCoordinateWeigher`

In this case, we want to emphasize schools, so here's our really simple implementation:

```
import java.util.Collection;
import com.berico.clavin.GeoParser;
import com.berico.clavin.GeoParserFactory;
import com.berico.clavin.Options;
import com.berico.clavin.extractor.LocationOccurrence;
import com.berico.clavin.gazetteer.FeatureCode;
import com.berico.clavin.resolver.ResolutionContext;
import com.berico.clavin.resolver.ResolvedCoordinate;
import com.berico.clavin.resolver.impl.strategies.coordinates.ResolvedCoordinateWeigher;

public class SchoolCoordinateWeigher implements ResolvedCoordinateWeigher {

  /**
   * Given a coordinate, reward entries that have the FeatureCode "SCH" (school), 
   * and penalize others.
   * @param item the result to evaluate.
   * @param context All co-occurring locations, to help provide context when weighing
   * the coordinate.
   * @param options If your weigher needs configuration, it can pull it from this
   * object.
   * @return weight used to influence the select of the coordinate.
   */
  @Override
  public double weigh(
      ResolvedCoordinate item,
      Collection<LocationOccurrence> context, 
      Options options) {
    
    // If this is a Spot feature.
    if (item.getKnownLocation()
        .getFeatureCode()
        .equals(FeatureCode.SCH)){
      
      // Give it an arbitrary bonus of "3".
      return 3;
    }
    
    // Otherwise, penalize the item.
    return -3;
  }
}
```

### Step 2 - Register the Weigher with the `GeoParserFactory`

Simply add the `ResolvedCoordinateWeigher` with the `GeoParserFactory` by adding it to the list of `DefaultCoordinateWeighers`:

```
GeoParserFactory.DefaultCoordinateWeighers.add(new SchoolCoordinateWeigher());
```

### Step 3 - Use the Standard `GeoParser` Workflow

Continue the normal workflow:

```
GeoParser parser = GeoParserFactory.getDefault("IndexDirectory/");

ResolutionContext context = 
  parser.parse(
    "I'm looking for a nice school around here: 38.9532, -77.3392.");

System.out.println(context.getCoordinates().get(0));
```

And you will see the following results:

<pre>
22:14:43.958 [main] INFO  com.berico.clavin.GeoParser - Input Size: 61
22:14:43.973 [main] INFO  com.berico.clavin.GeoParser - Extracted Location Count: 0
22:14:43.979 [main] DEBUG c.b.c.e.c.BaseDdPatternParsingStrategy - From '38.9532, -77.3392', parsed Lat/Lon: 38.9532, -77.3392.
22:14:43.981 [main] INFO  com.berico.clavin.GeoParser - Extracted Coordinates Count: 1
22:14:43.981 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Beginning resolution step.
22:14:43.981 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Found 0 location candidate lists.
22:14:44.946 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Found 1 coordinate candidate lists.
New Best: 0.09970562086930945, Loc: Resolved: "38.9532, -77.3392" as 20.05904965600212km S of National Training School for Women and Girls (historical), [US]
New Best: 0.11606821479011531, Loc: Resolved: "38.9532, -77.3392" as 17.231246328864234km WSW of Andrew Small Male and Female Academy (historical), [US]
New Best: 0.1168412863316477, Loc: Resolved: "38.9532, -77.3392" as 17.117237089662872km SSW of Bells Mill Elementary School, [US]
New Best: 0.1364719642499379, Loc: Resolved: "38.9532, -77.3392" as 14.65502464914444km SSW of Bullis School, [US]
New Best: 0.14195325200886444, Loc: Resolved: "38.9532, -77.3392" as 14.089145346773082km SSW of Connelly School of the Holy Child, [US]
New Best: 0.16636372506902752, Loc: Resolved: "38.9532, -77.3392" as 12.021851513424334km SSW of Manor Montessori School, [US]
New Best: 0.42611630520335275, Loc: Resolved: "38.9532, -77.3392" as 4.693554261073285km ENE of A Scott Crossfield Elementary School, [US]
New Best: 0.5840560914583524, Loc: Resolved: "38.9532, -77.3392" as 3.424328637693209km NW of Armstrong Elementary School, [US]
New Best: 0.6716661722289681, Loc: Resolved: "38.9532, -77.3392" as 2.977669685764982km W of Aldrin Elementary School, [US]
New Best: 1.2025737469077873, Loc: Resolved: "38.9532, -77.3392" as 0.27767851746865363km NNE of Carter Glass Library (historical), [US]
New Best: 9.454544034201737, Loc: Resolved: "38.9532, -77.3392" as 0.12941177659941908km ENE of Fairfax County Government Offices, [US]
New Best: 37.15426343228997, Loc: Resolved: "38.9532, -77.3392" as 0.046345362912706085km SW of Harvest Christian Fellowship Church, [US]
New Best: 1419.8423069941873, Loc: Resolved: "38.9532, -77.3392" as 0.0014086071320370847km NW of Sunset Hills Montessori School Childrens House, [US]
22:14:44.978 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Selected 1 coordinates.
22:14:44.980 [main] DEBUG c.b.c.r.impl.DefaultLocationResolver - Selected 0 locations.
22:14:44.980 [main] INFO  com.berico.clavin.GeoParser - Resolved 0 locations and 1 coordinates.
Resolved: "38.9532, -77.3392" as 0.0014086071320370847km NW of Sunset Hills Montessori School Childrens House, [US]
</pre>

### Summary

Applying weights to resolution can help you tune your extraction results without having to significantly rewrite the resolution algorithm.  More importantly, they were designed to be parallelized, weighing each candidate in isolation (not requiring all candidates to be presented to the weigher at once).  This makes it easier to scale, though it may have an impact on accuracy.


