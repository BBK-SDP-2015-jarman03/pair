
object Game extends App {

  private val SLEEP_INTERVAL = 10

  val p1= new AI(RED, 2);
  val p2 = new Dummy(YELLOW)
  val b = new Board();
  b.makeMove(new Move(RED, 4))
  b.makeMove(new Move(YELLOW, 4))
  b.makeMove(new Move(RED, 4))
  b.makeMove(new Move(YELLOW, 4))
  b.makeMove(new Move(RED, 4))
  b.makeMove(new Move(YELLOW, 4))

  var state = new State(RED, new Board(b), new Move(YELLOW, 4))
  AI.createGameTree(state, 3)
  AI.minimax(p1, state)
  println(state)
  println("Completed")
  //var children = state.getChildren
  //println(children.length)
//  for(child <- children)
//    println(child)

//  var posMovesRed = b.getPossibleMoves(RED)
//  var posMovesYellow = b.getPossibleMoves(YELLOW)
//
//  for(rMove <- posMovesRed){
//    println(s"${rMove.player} : ${rMove.column}")
//  }
//
//  for(yMove <- posMovesYellow){
//    println(s"${yMove.player} : ${yMove.column}")
//  }

  //val game = new Game(p1, p2, b, true)
  //game.runGame()
}

class Game(private var activePlayer: Solver, private var player2: Solver) {

  private var board: Board = new Board()

  private var gui: GUI = _

  private var winner: Player = _

  def this(p1: Solver,
    p2: Solver,
    b: Board,
    p: Boolean) {
    this(p1, p2)
    board = b
    activePlayer = (if (p) p1 else p2)
  }

  def setGUI(gui: GUI) {
    this.gui = gui
  }

  def columnClicked(col: Int) {
    if (activePlayer.isInstanceOf[Human]) {
      activePlayer.asInstanceOf[Human].columnClicked(col)
    }
  }

  def runGame() {
    while (!isGameOver) {
      var moveIsSafe = false
      var nextMove: Move = null
      while (!moveIsSafe) {
          val bestMoves = activePlayer.getMoves(board)
        if (bestMoves.length == 0) {
          gui.setMsg("Game cannot continue until a Move is produced.")
          //continue
        } else {
          nextMove = bestMoves(0)
        }
        if (board.getTile(0, nextMove.column) == null) {
          moveIsSafe = true
        } else {
          gui.setMsg("Illegal Move: Cannot place disc in full column. Try again.")
        }
      }
      board.makeMove(nextMove)
      if (gui == null) {
        println(nextMove)
        println(board)
      } else {
        gui.updateGUI(board, nextMove)
      }
      val temp = activePlayer
      activePlayer = player2
      player2 = temp
      try {
        Thread.sleep(Game.SLEEP_INTERVAL)
      } catch {
        case e: InterruptedException => e.printStackTrace()
      }
    }
    if (gui == null) {
      if (winner == null) {
        println("Tie game!")
      } else {
        println(winner + " won the game!!!")
      }
    } else {
      gui.notifyGameOver(winner)
    }
  }

  def isGameOver(): Boolean = {
    winner = board.hasConnectFour()
    if (winner != null) return true
    var r = 0
    while (r < Board.NUM_ROWS) {
      var c = 0
      while (c < Board.NUM_COLS) {
        if (board.getTile(r, c) == null) return false
        c = c + 1
      }
      r = r + 1
    }
    true
  }
}

