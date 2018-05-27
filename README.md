## Sprite Sheet Processor

Welcome to the (unfinished) Sprite Sheet Processor. This lil' tool will one day help you work more efficiently with sprite sheets by taking them apart, arranging them intelligently to conserve space and come with a metadata available in different formats (json, yaml, text). This way, all you have to do is load the sprite sheet and the metadata file and voila! You're ready to display every sprite in your sprite sheet without too much work on your part.

How much of it is done? You can unpack any SpriteSheet into individual sprites.

#### Usage

    Usage: SpriteSheetProcessor [options] [sprite sheets]
      Options:
        -pack (-p)
            - Create a sprite sheet with metadata file, any of {yaml, json, txt} (default: yaml)
        -verbose (-v)
            - Turn on debugging statements, but without logging to file (default: false)
        -debug (-d)
            - Turn on debugging statements with logging to file (default: false)
        -export-folder (-e) REQUIRED
            - Where to export the new files
        -fail-fast (-ff)
            - Verify integrity of files before attempting (un)packing (default: false)

#### Example

**Note:** Please ensure that the output directory exists before running the application.

    # Using relative path
    java -jar SpriteSheetProcessor.jar -e out/ UI.png

    # Using absolute path
    java -jar SpriteSheetProcessor.jar -e C:/out/ FX.png -v

    # Extract to directory currently in
    java -jar SpriteSheetProcessor.jar -e . Heroes.gif
    
    # Multiple sprite sheets (with fail-fast)
    java -jar SpriteSheetProcessor.jar -ff -e . Heroes.gif FX.png UI.png 

#### JavaFX (WIP)

This will let you confirm and edit (to an extent) any errors encountered by the unpacking process.

So far only unpacking is supported- sprite packing is coming soon!

![JavaFX App][1]

#### Upcoming Features

1. Click and drag to select sprites in the JavaFX App.
2. Add functionality to Combined, Separate and Exclude (probably removing Separate) in the JavaFX App.
3. Add Packing functionality to the JavaFX App.
4. User-configurable options to help detecting sprites.

#### Changelog

**2018-05-27**

* Improved sprite detection and packing density in some cases.
    * Now removed sprites wholly contained inside other sprites
    * Merges sprites that share an edge of their sprite boundaries
    * Packing now packs the sprites from largest to smallest

**2018-02-19**

* Color Distance implemented, can now reliably cut .jpeg and other lossy formats.

**2018-02-17**

* Sprite Sheet Packing has been added to the console version.

**2018-02-16**

* Sprite Sheet Packing algorithm v1 is complete.

**2016-05-26**

* Export of individual sprites to file via JavaFX app.

**2016-05-25**

* Drag'n'Drop functionality now included in JavaFX app.
* Performance improvements during unpacking for most sprite sheets.

**2016-05-24**

* Experimental JavaFX integration. Includes basic UI with no functionality. Checkout the project and run the
  main method in *SpriteSheetProcessorApp*.
* Small sprites, especially those operating in single-pixel dimensions, should now be accurately detected and extracted.

*Licensed under Apache 2.0*

[1]: http://i.imgur.com/r0XSClG.png