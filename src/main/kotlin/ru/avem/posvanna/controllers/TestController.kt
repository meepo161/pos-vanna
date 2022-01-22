package ru.avem.posvanna.controllers

import ru.avem.posvanna.communication.model.CommunicationModel
import ru.avem.posvanna.communication.model.IDeviceController
import ru.avem.posvanna.communication.model.devices.owen.pr.OwenPrController
import ru.avem.posvanna.communication.model.devices.owen.trm136.Trm136Controller
import ru.avem.posvanna.communication.model.devices.parma.ParmaController
import tornadofx.Controller

abstract class TestController : Controller() {
    protected val owenPR = CommunicationModel.device<IDeviceController>(CommunicationModel.DeviceID.DD2) as OwenPrController
    protected val parma1 = CommunicationModel.device<IDeviceController>(CommunicationModel.DeviceID.PARMA1) as ParmaController
    protected val trm1 = CommunicationModel.device<IDeviceController>(CommunicationModel.DeviceID.TRM1) as Trm136Controller
    protected val trm2 = CommunicationModel.device<IDeviceController>(CommunicationModel.DeviceID.TRM2) as Trm136Controller
    protected val trm3 = CommunicationModel.device<IDeviceController>(CommunicationModel.DeviceID.TRM3) as Trm136Controller
}
