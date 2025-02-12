package o1.adventure

import scala.collection.mutable.Map

/** A `Player` object represents a player character controlled by the real-life user
  * of the program.
  *
  * A player object’s state is mutable: the player’s location and possessions can change,
  * for instance.
  *
  * @param startingArea  the player’s initial location */
class Player(startingArea: Area):

  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag
  private var kamat = Map[String, Item]()
  private var vaatteet = Map[String, Item]()
  private var vatsa = Map[String, Item]()
  var starvingLimit = 12 // The maximum number of turns before the dog starves to death.
  var timeLimit = 25 // The maximum number of turns that this adventure game allows before time runs out.

  def inventory: String =
    if kamat.isEmpty then
      "You don't have your friends nor clothing with you."
    else
      s"You got: \n" + kamat.keys.mkString("\n")

  def get(itemName: String): String =
    if currentLocation.contains(itemName) && itemName == "swimming trunks" then
      "You guys have to wear them by 'using' them."
    else if currentLocation.contains(itemName) && itemName == "friends" then
      val thing = currentLocation.removeItem(itemName)
      kamat.put(itemName, thing.get)
      s"You picked up your $itemName."
    else
      s"There is no $itemName here to pick up."

  def drop(itemName: String): String =
    if kamat.contains(itemName) then
      val thing = kamat.remove(itemName)
      currentLocation.addItem(thing.get)
      s"You drop the $itemName"
    else
      "You don't have that!"

  def has(itemName: String): Boolean =
    kamat.contains(itemName)

  def examine(itemName: String): String =
    if kamat.contains(itemName) then
      s"You look closely at $itemName.\n${kamat(itemName).description}"
    else
      "If you want to examine something, you need to pick it up first."

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven

  /** Returns the player’s current location. */
  def location = this.currentLocation


  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player’s current location towards the direction name. Returns
    * a description of the result: "You go DIRECTION." or "You can't go DIRECTION." */
  def go(direction: String) =
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation)
    if destination.isDefined then "You go " + direction + "." else "You can't go " + direction + "."


  /** Causes the player to rest for a short while (this has no substantial effect in game terms).
    * Returns a description of what happened. */
  def rest() =
    "You rest for a while. Better get a move on, though."


  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def quit() =
    this.quitCommandGiven = true
    ""

  def use(itemName: String) =
    if  currentLocation.contains(itemName) && itemName == "swimming trunks" then
      val vaate = currentLocation.removeItem(itemName)
      kamat.put(itemName, vaate.get)
      vaatteet.put(itemName, vaate.get)
      "Swimming trunks are on!"
    else
      "Can't use this."

  def hasOn(itemName: String): Boolean =
    vaatteet.contains(itemName)

  def hasEaten(itemName: String): Boolean =
    vatsa.contains(itemName)

  def eat(itemName: String) =
    if currentLocation.contains(itemName) && itemName == "food" then
      val safka = currentLocation.removeItem(itemName)
      vatsa.put(itemName, safka.get)
      starvingLimit += 10
      "Yummy, what a burger!😋"
    else
      "Can't eat that."

  /** Returns a brief description of the player’s state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name


end Player

