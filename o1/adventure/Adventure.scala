package o1.adventure

/** The class `Adventure` represents text adventure games. An adventure consists of a player and
  * a number of areas that make up the game world. It provides methods for playing the game one
  * turn at a time and for checking the state of the game.
  *
  * N.B. This version of the class has a lot of “hard-coded” information that pertains to a very
  * specific adventure game that involves a small trip through a twisted forest. All newly created
  * instances of class `Adventure` are identical to each other. To create other kinds of adventure
  * games, you will need to modify or replace the source code of this class. */
class Adventure:

  /** the name of the game */
  val title = "Otaniemen Koira"

  private val alvarinaukio  = Area("Alvarinaukio", "You're in Alvarinaukio. The beautiful Sun is shining🌞")
  private val abloc         = Area("A-Bloc", "A-Bloc is full of dogs sleeping on the floor. Alepa is selling limited edition swimwear🩲")
  private val dipoli        = Area("Dipoli", "There is an afterafter party in Dipoli🕺 It's tempting, but we have a mission!")
  private val xburger       = Area("X-Burger", "Smells like heaven in X-Burger! There's lots of options, but your favorite is the 'DoggyBurger'🍔")
  private val smokki        = Area("Smökki", "Smells like vomit here in Smökki and it's stuffy af in here🤮")
  private val otaranta      = Area("Otaranta", "The beautiful OtaBeach!🌊❤️")
  private val destination   = otaranta

  alvarinaukio  .setNeighbors(Vector( "north" -> abloc,        "east" -> smokki,       "south" -> dipoli,       "west" -> xburger ))
  abloc         .setNeighbors(Vector(                          "east" -> smokki,       "south" -> alvarinaukio                    ))
  dipoli        .setNeighbors(Vector( "north" -> alvarinaukio, "east" -> otaranta                                                 ))
  xburger       .setNeighbors(Vector(                          "east" -> alvarinaukio                                             ))
  smokki        .setNeighbors(Vector(                                                  "south" -> otaranta,     "west" -> alvarinaukio   ))
  otaranta      .setNeighbors(Vector( "north" -> smokki,                                                        "west" -> dipoli  ))


  private val friends = Item("friends", "Your lovely friends!")
  smokki.addItem(friends)
  private val food = Item("food", "It's the 'DoggyBurger', that you and your friends need!")
  xburger.addItem(food)
  private val swimmingtrunks = Item("swimming trunks", "Ready for swimming!")
  abloc.addItem(swimmingtrunks)

  /** The character that the player controls in the game. */
  val player = Player(alvarinaukio)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0




  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete =
    this.player.location == this.destination && player.has("friends") && player.hasEaten("food") && player.hasOn("swimming trunks")

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver = this.isComplete || this.player.hasQuit || this.turnCount == this.player.timeLimit || this.turnCount == this.player.starvingLimit

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage =
    "You woke up in the middle of Alvarinaukio. It's a beautiful summer morning and you've got the biggest hangover." +
    " You need your friends, food and some swimming trunks in which you will be swimming at Otaranta!"


  /** Returns a message that is to be displayed to the player at the end of the game. The message
    * will be different depending on whether or not the player has completed their quest. */
  def goodbyeMessage =
    if this.isComplete then
      "You made it!! So refreshing!😄"
    else if this.turnCount == this.player.starvingLimit && !player.hasEaten("food") then
      "You starved to death:( \nGame over!"
    else if this.turnCount == this.player.timeLimit then
      "You or your friends died of dehydration (didn't make it to the beach) :( \nGame over!"
    else
      "Loser!" // game over due to player quitting



  /** Plays a turn by executing the given in-game command, such as “go west”. Returns a textual
    * report of what happened, or an error message if the command was unknown. In the latter
    * case, no turns elapse. */
  def playTurn(command: String) =
    val action = Action(command)
    val outcomeReport = action.execute(this.player)
    if outcomeReport.isDefined then
      this.turnCount += 1
    outcomeReport.getOrElse(s"Unknown command: \"$command\".")

end Adventure

