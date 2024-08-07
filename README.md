![Forge Logo](docs/assets/logo.png)

# Minecraft Bedrock Geometry Java Parser
It's exactly what it sounds like, just a simple lightweight **library** (not a GUI app) that turns a **[Minecraft]** Bedrock `.geo.json` model into a **[Java]** `object` and make the 3D model data way easier to access without the need to parse it yourself.

[![GitHub License](https://img.shields.io/github/license/El-Karto-Muesca/MCBE-Geometry-Parser)](https://github.com/El-Karto-Muesca/MCBE-Geometry-Parser?tab=MIT-1-ov-file)
[![GitHub Release](https://img.shields.io/github/v/release/El-Karto-Muesca/MCBE-Geometry-Parser)][Download]


# 1. How to use:
This assumse that you already familiar with the Bedrock geometry JSON structure.
## 1. 1. Methods!
``` Java
// Just call the method "parse()" 
BedrockGeometry geo = BedrockGeometry.parse("../path/to/something.geo.json");
```
*That's it! just a single line of code, Well not really, you still gonna have to handle some exceptions,
but other than that, that's pretty much it!*

``` java
// Wanna know how many bones are in a geometry object?
int boneCount = geo.getGeometry()[0].getBoneCount();

// Wanna know the X coordinate of the origin of the first cube  
// of the first bone of the first geometry of the model?
float boneCount = geo.getGeometry()[0].getBones()[0].getCubes()[0].getOrigin().getX()
```
*You got the point :)*

But wait there is more, if you wanna get the parent of a certain bone, check this out:
``` Java
BedrockGeometry.Bone bone = geo.getGeometry()[0].getBones()[0];

BedrockGeometry.Bone parent = bone.getParent();
```
*The method returns a Bone object!*

Another thing I thought it would be useful is knowing the total amount of cube in a geometry object:

``` Java
int boneCount = geo.getGeometry()[0].getCubeCount();
```

## 1. 2. Exceptions!

The method will of course throw some exceptions, and you gonna have to handle them all with a good old `try_catch` expression like so:

``` java
String filepath = "../path/to/theseNuts.geo.json";

try {
    BedrockGeometry geo = BedrockGeometry.parse(filepath);
} catch (Exception e) {
    // handle exceptions here...
}
```
*Handling all exceptions in one catch.*
``` java
String filepath = "../path/to/theseNuts.geo.json";

try {
    BedrockGeometry geo = BedrockGeometry.parse(filepath);
} catch (FileNotFoundException e) {
    // This is self-explanatory, I'm sure you can... handle it! :D
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
*Handling each exception on its own.*

# 2. Good to know info:
## 2. 1. Supported formats:

The supported format version are `1.16.0`, `1.12.0`, `1.10.0` and `1.8.0`, which to my knowledge are the only version out there? at least within the vanilla bedrock resourcepack, and **[BlockBench]** only exports bedrock geometry in `1.12.0` and `1.10.0` for legacy.

So practicaly all versions are supported?

Still the parcer will do a version check just in case and will throw an `UnsupportedFormatException` if it's none of the format versions above.

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
    "format_version": "ur.ma.ma",
    "geometry.bat": {}
}
```
*This is NOT gonna work! and will throw an `UnsupportedFormatException` :(*

## 2. 2. Data Types:
* All data is either stored as a `boolean`, `int`, `float` or a `String`, no `double` and `long`. 
* There are some Vector classes such as `Vec3f` and `Vec2i` but they are just there to make data access easier.

# 3. Motivation:
Please note that the reason for the existence of this lib is that I needed a tool that can do the same, and I couldn't find any, so I wrote it, and spent a little extra to make it public and available for anyone to use, even tho I doubt that there is someone who would need this, but hey if it helps at least one person somewhere in the world, that's a win for me :)!.

<sub>This Repository is **NOT** affiliated or endorsed by Mojang AB or Microsoft Inc. Mojang and Minecraft are trademarks of Mojang AB.</sub>

[//]: # (Reusable Links Section)

[Download]: https://github.com/El-Karto-Muesca/MCBE-Geometry-Parser/releases/download/1.0/MCBE-Geometry-Parser-1.0.jar

[Java]: https://www.java.com/en/
[Minecraft]: https://www.minecraft.net/en-us
[BlockBench]: https://www.blockbench.net/
