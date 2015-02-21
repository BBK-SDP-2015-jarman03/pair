import java.util.List
import AI._

import scala.collection.mutable.ListBuffer

//remove if not needed
import scala.collection.JavaConversions._

object AI {

  def createGameTree(s: State, d: Int): Unit = {
    if(d == 0) return
    s.initializeChildren()
    val children = s.getChildren
    for(child <- children)
      createGameTree(child, d-1)
  }

  def minimax(ai: AI, s: State) {
    ai.minimax(s)
  }
}

class AI(private var player: Player, private var depth: Int) extends Solver {

  override def getMoves(b: Board): Array[Move] = {
    var moves =  new ListBuffer[Move]()
    var state = new State(player, b, null)
    createGameTree(state, depth)
    minimax(state)
    var children = state.getChildren
    var first = true
    var value = 0;
    for(child <- children){
      if(first){
        moves.append(child.getLastMove)
        value = child.getValue
        first = false
      } else if (value < child.getValue) {
        moves = new ListBuffer[Move]()
        moves.append(child.getLastMove)
        value = child.getValue
      } else if (value == child.getValue){
        moves.append(child.getLastMove)
      }
    }
    moves.toArray
  }

  def minimax(s: State): Unit = {

    def helper(s1: State): Int = {
      if(s1.getChildren.length == 0){
        s1.setValue(evaluateBoard(s1.getBoard))
        s1.getValue
      }else{
        val children = s1.getChildren
        val listOfValues = new ListBuffer[Integer]
        for(child <- children)
          listOfValues.append(helper(child))
        if(s1.player == this.player)
          s1.setValue(listOfValues.max)
        else
          s1.setValue(listOfValues.min)
        s1.getValue
      }
    }
    s.setValue(helper(s))
  }

  def evaluateBoard(b: Board): Int = {
    val winner = b.hasConnectFour()
    var value = 0
    if (winner == null) {
      val locs = b.winLocations()
      for (loc <- locs; p <- loc) {
        value += (if (p == player) 1 else if (p != null) -1 else 0)
      }
    } else {
      var numEmpty = 0
      var r = 0
      while (r < Board.NUM_ROWS) {
        var c = 0
        while (c < Board.NUM_COLS) {
          if (b.getTile(r, c) == null) numEmpty += 1
          c = c + 1
        }
        r = r + 1
      }
      value = (if (winner == player) 1 else -1) * 10000 * numEmpty
    }
    value
  }
}

