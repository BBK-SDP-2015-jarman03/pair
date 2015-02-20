/**
 * Created by mike on 20/02/15.
 */
object testing extends App {
  val p1 = new Dummy(RED)
  val p2 = new Dummy(YELLOW)
  val b = new Board();
  b.makeMove(new Move(RED, 4))
  b.makeMove(new Move(YELLOW, 4))
  b.makeMove(new Move(RED, 4))
  b.makeMove(new Move(YELLOW, 4))
  b.makeMove(new Move(RED, 4))
  var move = new Move(YELLOW, 4)
  b.makeMove(move)

  var state = new State(RED, b, move)
  AI.createGameTree(state, 4)


  //val game = new Game(p1, p2, b, true)
  //game.runGame()
}
