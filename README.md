## Sprite Sheet Processor

Welcome to the (unfinished) Sprite Sheet Processor. This lil' tool will one day help you work more efficiently with sprite sheets by taking them apart, arranging them intelligently to conserve space and come with a metadata available in different formats (json, yaml, text). This way, all you have to do is load the sprite sheet and the metadata file and voila! You're ready to display every sprite in your sprite sheet without too much work on your part.

How much of it is done? You can unpack any SpriteSheet into individual sprites. .png is supported, testing for other formats (e.g. .gif) is still underway.

#### Usage

    Usage: SpriteSheetProcessor [options] [sprite sheets]
      Options:
        -metadata-output-format (-mof) 
            - Structure of the metadata file, any of {yaml, json, txt} (default: yaml)
        -verbose (-v)
            - Turn on debugging statements, but without logging to file (default: false)
        -debug (-d)
            - Turn on debugging statements with logging to file (default: false)
        -export-folder (-e) REQUIRED
            - Where to export the new files

#### Example

**Note:** Please ensure that the output directory exists before running the application.

    # Using relative path
    java -jar SpriteSheetProcessor.jar -e out/ UI.png

    # Using absolute path
    java -jar SpriteSheetProcessor.jar -e C:/out/ FX.png -v

    # Extract to directory currently in
    java -jar SpriteSheetProcessor.jar -e . Heroes.gif


#### Updates

**2016-05-24**

* Experimental JavaFX integration. Includes basic UI with no functionality. Checkout the project and run the
  main method in *com.sbg.rpg.ui.SpriteSheetProcessorApp*.
* Small sprites, especially those operating in single-pixel dimensions, should now be accurately detected and extracted.

*Licensed under Apache 2.0*