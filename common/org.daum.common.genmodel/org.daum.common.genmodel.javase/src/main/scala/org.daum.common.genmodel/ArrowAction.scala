package org.daum.common.genmodel

trait ArrowAction extends Action {
  private var actionType : ArrowActionType = ArrowActionType.FIRE

  def getActionType : ArrowActionType ={
    this.actionType
  }

  def setActionType(actionType : ArrowActionType) {
    this.actionType = actionType
  }
}