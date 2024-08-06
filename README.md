![Forge Logo](docs/assets/logo.png)

# Minecraft Bedrock Geometry Java Parser
It's exactly what it sounds like, just a simple lightweight **library** (not a GUI app) that turns a **[Minecraft]** Bedrock `.geo.json` model into a **[Java]** `object` and make the 3D model data way easier to access without the need to parse it yourself.

[![GitHub License](https://img.shields.io/github/license/El-Karto-Muesca/MCBE-Geometry-Parser)](https://github.com/El-Karto-Muesca/MCBE-Geometry-Parser?tab=MIT-1-ov-file)
[![GitHub Release](https://img.shields.io/github/v/release/El-Karto-Muesca/MCBE-Geometry-Parser)][Download]
[![Static Badge](https://img.shields.io/badge/Supported_format-1.12.0+-red)](#)


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
String filepath = "../path/to/something.geo.json";

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

# 2. Good to know info:
## 2. 1. Supported formats:

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

## 2. 2. Data Types:
* All data is either stored as a `boolean`, `int`, `float` or a `String`, no `double` and `long`. 
* There are some Vector classes such as `Vec3f` and `Vec2i` but they are just there to make data access easier.

# 3. Motivation:
Please note that the reason for the existence of this lib is that I needed a tool that can do the same, and I couldn't find any, so I wrote it, and spent a little extra to make it public and available for anyone to use, even tho I doubt that there is someone who would need this, but hey if it helps at least one person somewhere in the world, that's a win for me :)!.

# 4. Contributing:
Feel free to contribute to the project, even tho to be honest, other than the support for earlier formats, nothing really comes to mind to be added to the project, but who knows I might be wrong ¯\\(ツ)/¯. 


<sub>This Repository is **NOT** affiliated or endorsed by Mojang AB or Microsoft Inc. Mojang and Minecraft are trademarks of Mojang AB.</sub>

[//]: # (Reusable Links Section)

[Download]: https://github.com/El-Karto-Muesca/MCBE-Geometry-Parser/releases/download/1.0/MCBE-Geometry-Parser-1.0.jar

[Java]: https://www.java.com/en/
[Minecraft]: https://www.minecraft.net/en-us
[BlockBench]: https://www.blockbench.net/
