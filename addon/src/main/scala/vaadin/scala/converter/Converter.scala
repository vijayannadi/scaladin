package vaadin.scala.converter

import scala.reflect.Manifest
import java.util.Locale
import vaadin.scala.Wrapper
import vaadin.scala.mixins.ScaladinMixin
import vaadin.scala.converter.mixins.{ DelegatingConverterMixin, ConverterMixin }

package mixins {
  trait ConverterMixin[Presentation, Model] extends ScaladinMixin
  trait DelegatingConverterMixin[Presentation, Model] extends ConverterMixin[Presentation, Model] { self: com.vaadin.data.util.converter.Converter[Presentation, Model] =>

    override def wrapper = super.wrapper.asInstanceOf[Converter[Presentation, Model]]

    //    type extendsModel = 
    override def convertToModel(value: Presentation, targetType: Class[_ <: Model], locale: Locale): Model =
      wrapper.convertToModel(Option(value), locale).getOrElse(null).asInstanceOf[Model]

    override def convertToPresentation(value: Model, targetType: Class[_ <: Presentation], locale: Locale): Presentation =
      wrapper.convertToPresentation(Option(value), locale).getOrElse(null).asInstanceOf[Presentation]

    override def getPresentationType: Class[Presentation] = wrapper.presentationType

    override def getModelType: Class[Model] = wrapper.modelType
  }
}

/**
 * @see com.vaadin.data.util.converter.Converter
 * @author Henri Kerola / Vaadin
 */
abstract class Converter[Presentation: Manifest, Model: Manifest](val p: com.vaadin.data.util.converter.Converter[Presentation, Model] with ConverterMixin[Presentation, Model] = new com.vaadin.data.util.converter.Converter[Presentation, Model] with DelegatingConverterMixin[Presentation, Model])
    extends Wrapper {

  p.wrapper = this

  private var presentationManifest: Option[Manifest[Presentation]] = None
  private var modelManifest: Option[Manifest[Model]] = None
  setManifests()

  private def setManifests()(implicit presentationManifest: Manifest[Presentation], modelManifest: Manifest[Model]) {
    this.presentationManifest = Option(presentationManifest)
    this.modelManifest = Option(modelManifest)
  }

  def convertToPresentation(value: Option[Model], locale: Locale): Option[Presentation]

  def convertToModel(value: Option[Presentation], locale: Locale): Option[Model]

  def presentationType: Class[Presentation] = presentationManifest.get.runtimeClass.asInstanceOf[Class[Presentation]]

  def modelType: Class[Model] = modelManifest.get.runtimeClass.asInstanceOf[Class[Model]]
}
abstract class DeletagePeerConverter[Presentation: Manifest, Model: Manifest](override val p: com.vaadin.data.util.converter.Converter[Presentation, Model] with ConverterMixin[Presentation, Model]) extends Converter[Presentation, Model](p) {

  def convertToPresentation(value: Option[Model], locale: Locale): Option[Presentation] =
    Option(p.convertToPresentation(value.getOrElse(null).asInstanceOf[Model], presentationType, locale))

  def convertToModel(value: Option[Presentation], locale: Locale): Option[Model] =
    Option(p.convertToModel(value.getOrElse(null).asInstanceOf[Presentation], modelType, locale))

}