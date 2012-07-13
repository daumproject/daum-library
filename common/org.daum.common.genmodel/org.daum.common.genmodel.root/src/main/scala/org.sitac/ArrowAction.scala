package org.sitac

trait ArrowAction extends Action {
  private var actionType : ArrowActionType = ArrowActionType.FIRE

  def getActionType : ArrowActionType ={
    this.actionType
  }

  def setActionType(actionType : ArrowActionType) {
    this.actionType = actionType
  }

  override def getClonelazy(subResult : java.util.IdentityHashMap[Object,Object]): Unit = {
    super.getClonelazy(subResult)
    // TODO I should definitely do something here
  }
  override def resolve(addrs : java.util.IdentityHashMap[Object,Object]) : Action = {
    super.resolve(addrs)
    // TODO I should definitely do something here
  }
}