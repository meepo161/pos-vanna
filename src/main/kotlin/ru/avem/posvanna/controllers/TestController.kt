package ru.avem.posvanna.controllers

import ru.avem.posvanna.communication.model.CommunicationModel
import ru.avem.posvanna.communication.model.CommunicationModel.getDeviceById
import ru.avem.posvanna.communication.model.devices.owen.pr.OwenPrController
import ru.avem.posvanna.communication.model.devices.owen.trm136.Trm136Controller
import ru.avem.posvanna.communication.model.devices.parma.ParmaController
import tornadofx.Controller

abstract class TestController : Controller() {
    protected val owenPR = getDeviceById(CommunicationModel.DeviceID.DD2) as OwenPrController
    protected val parma1 = getDeviceById(CommunicationModel.DeviceID.PARMA1) as ParmaController
    protected val parma2 = getDeviceById(CommunicationModel.DeviceID.PARMA2) as ParmaController
    protected val parma3 = getDeviceById(CommunicationModel.DeviceID.PARMA3) as ParmaController
    protected val parma4 = getDeviceById(CommunicationModel.DeviceID.PARMA4) as ParmaController
    protected val parma5 = getDeviceById(CommunicationModel.DeviceID.PARMA5) as ParmaController
    protected val parma6 = getDeviceById(CommunicationModel.DeviceID.PARMA6) as ParmaController
    protected val trm1 = getDeviceById(CommunicationModel.DeviceID.TRM1) as Trm136Controller
    protected val trm2 = getDeviceById(CommunicationModel.DeviceID.TRM2) as Trm136Controller
    protected val trm3 = getDeviceById(CommunicationModel.DeviceID.TRM3) as Trm136Controller

}
