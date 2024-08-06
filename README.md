> This Repository is **NOT** affiliated or endorsed by Mojang AB or Microsoft Inc. Mojang and Minecraft are trademarks of Mojang AB.
---
![Forge Logo](docs/assets/logo.png)

# Minecraft Bedrock Geometry Java Parser
It's exactly what it sounds like, just a simple lightweight **library** (not a GUI app) that turns a **[Minecraft]** Bedrock `.geo.json` model into a **[Java]** `object`.

![GitHub License](https://img.shields.io/github/license/El-Karto-Muesca/McBedrock-Geo-Parser)
![Static Badge](https://img.shields.io/badge/v0.1.0_alpha-Download_JAR-blue?style=flat)
![Static Badge](https://img.shields.io/badge/Supported_%22format__version%22-1.12.0_or_higher-red)


## 1. How to use:

Use the parse method `BedrockGeometry.parse()` to get a `BedrockGeometry` which you can use to access all the properties of the geo models.

The method is well documented almost everything is commented.

``` java
// Get the path to your file, must have ".geo.json" at the end!
String filepath = "../path/to/something.geo.json";

// Then call the method "parse" with the file path
BedrockGeometry geo = BedrockGeometry.parse(filepath);

// And that's it! :D
// Well not really, you still gonna have to handle some exceptions :P
// But other than that, that's pretty much it!

// Wanna know the format version of the geo?
String format = geo.getFormatVersion(); 

// Wanna know how many bones are in a geometry object?
int boneCount = geo.getGeometry()[0].getBoneCount();

// Wanna know how many cubes are in a geometry object?
int boneCount = geo.getGeometry()[0].getCubeCount();

// Wanna know the X coordinate of the origin of the first cube  
// of the first bone of the first geometry of the model?
float boneCount = geo.getGeometry()[0].getBones()[0].getCubes()[0].getOrigin().getX()

// You got the point :)
```
### Exceptions!
The method `parse(...)` will of course throw some exceptions, and you gonna have to handle them all with a good old `try_catch` expression like so:

``` java
String filepath = "../path/to/something.geo.json";

try {
    BedrockGeometry geo = BedrockGeometry.parse(filepath);
} catch (FileNotFoundException e) {
    // This is self-explanatory, you can handle it! :D
    // handle exception here...
} catch (IOException e) {
    // Mostly due to interrupted IO operation.
    // handle exception here...
} catch (ParseException e) {
    // Due to invalid JSON, mostly a syntax error.
    // handle exception here...
} catch (UnsupportedFormatException e) {
    // The parser does support all bedrock geo formats, explained further below.
    // handle exception here...
} catch (InvalidGeometryException e) {
    // Due to not finding a major property of the geometry.
    // handle exception here...
}
```

## 2. Supported formats:

Not all format versions are supported! Only `1.12.0` or **higher**.

### Example:
``` JSON
{
    "format_version": "1.12.0",
    "minecraft:geometry": []
}
```
*This is gonna work just fine! :D*
``` JSON
{
    "format_version": "1.8.0",
    "geometry.bat": {}
}
```
*This is NOT gonna work! and will throw an `UnsupportedFormatException` :(*


Why? Well I didn't think it would be needed since **[BlockBench]** exports Bedrock geo in `1.12.0` format, 

I am aware though that some vanilla models in the ResourcePack still use format versions as early as `1.8.0`, and we might be able to add support for such format in the future.

But for now, a solution to that would be to use **[BlockBench]** to export them as bedrock models and I believe the format version would be converted to `1.12.0` or **higher**!

## 3. You might care to know that:
* All data is either stored in the form of `boolean`, `int`, `float` or a `String`, no `double` and `long`. there are some Vector classes such as `Vec3f` and `Vec2i` but they are just there to make data access easier.

## 4. Contributing:
Feel free to contribute to the project, even tho to be honest, other than the support for earlier formats, nothing really comes to mind to be added to the project, but who knows I might be wrong ¯\\_(ツ)_/¯. 


---
<sub>This Repository is **NOT** affiliated or endorsed by Mojang AB or Microsoft Inc. Mojang and Minecraft are trademarks of Mojang AB.</sub>

[//]: # (Reusable Links Section)

[Download]: https://www.java.com/en/

[Java]: https://www.java.com/en/
[Minecraft]: https://www.minecraft.net/en-us
[BlockBench]: https://www.blockbench.net/