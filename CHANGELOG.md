# Versions Changelog

## v1.0.1

* Fix the world spawn position not updating when a player died with an obstructed spawn point (such as bed)

## v1.0

* World spawn position can be updated depending on living players' position
  * A shape in which the position is taken can be chosen
    * BOX: A box containing every player
    * CIRCLE: A circle centered on the box, with a radius equals to the longest side of the box
  * A player influence to tweak the shape
    * NONE: No player influence on the shape
    * AREA OFFSET: The shape is moved toward the weighted center
    * AREA SHRINK: The shape is shrink in a way that makes the shape's center match the weighted one
  * A distance to expand the shape in each direction
* World spawn chunks can be fixed or depends on world spawn position
