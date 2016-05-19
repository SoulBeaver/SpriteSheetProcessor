## Sprite Sheet Processor

Welcome to the (unfinished) Sprite Sheet Processor. This lil' tool will one day help you work more efficiently with sprite sheets by taking them apart, arranging them intelligently to conserve space and come with a metadata available in different formats (json, yaml, text). This way, all you have to do is load the sprite sheet and the metadata file and voila! You're ready to display every sprite in your sprite sheet without too much work on your part.

How much of it is done? Not much. You can convert a text metadata file from [Sprite Sheet Packer](http://spritesheetpacker.codeplex.com/) to yaml and it extracts your sprites via the command line.

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

*Licensed under Apache 2.0*