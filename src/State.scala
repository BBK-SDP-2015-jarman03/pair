import java.io.FileNotFoundException
import java.io.PrintWriter
import java.io.UnsupportedEncodingException
import State._
import scala.beans.BeanProperty
import scala.collection.mutable.ListBuffer

object State {

  val length0 = Array[State]()
}

class State(@BeanProperty var player: Player, @BeanProperty var board: Board, @BeanProperty var lastMove: Move)
  extends Comparable[Any] {

  @BeanProperty
  var children: Array[State] = length0

  @BeanProperty
  var value: Int = 0

  def initializeChildren(): Unit = {

    val moves = board.getPossibleMoves(player)
    val stateList = new ListBuffer[State]
    for(move <- moves){
      val childBoard = new Board(board)
      childBoard.makeMove(move)
      stateList.append(new State(player.opponent, childBoard, move))
    }

    children = stateList.toArray

    //getpossiblemoves from this board and player
    //for each move
    //new board(board)
    //board.makemove(move)
    //children[] :+ new State()


    //this.player
    //this.board
    //this.value
    //this.lastmove
  }

  def writeToFile() {
    try {
      var writer = new PrintWriter("output.txt", "UTF-8")
      writer.println(this)
      java.awt.Toolkit.getDefaultToolkit.beep()
    } catch {
      case e @ (_: FileNotFoundException | _: UnsupportedEncodingException) => e.printStackTrace()
    }
  }

  override def toString(): String = {
    println("State.toString printing")
    toStringHelper(0, "")
  }

  private def toStringHelper(d: Int, ind: String): String = {
    var str = ind + player + " to play\n"
    str = str + ind + "Value: " + value + "\n"
    str = str + board.toString(ind) + "\n"
    if (children != null && children.length > 0) {
      str = str + ind + "Children at depth " + (d + 1) + ":\n" + ind +
        "----------------\n"
      for (s <- children) {
        str = str + s.toStringHelper(d + 1, ind + "   ")
      }
    }
    str
  }

  override def compareTo(ob: AnyRef): Int = 0
}

