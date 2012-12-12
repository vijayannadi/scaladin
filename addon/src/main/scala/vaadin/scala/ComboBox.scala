package vaadin.scala

import vaadin.scala.mixins.ComboBoxMixin

package mixins {
  trait ComboBoxMixin extends AbstractSelectMixin { self: com.vaadin.ui.ComboBox => }
}

object ComboBox {
  object FilterinMode extends Enumeration {
    import com.vaadin.shared.ui.combobox.FilteringMode._
    val Off = Value(OFF.ordinal)
    val StartsWith = Value(STARTSWITH.ordinal)
    val Contains = Value(CONTAINS.ordinal)
  }
}

// In Vaadin ComboBox extends Select, here just extend AbstractSelect directly and add methods from Select to ComboBox
class ComboBox(override val p: com.vaadin.ui.ComboBox with ComboBoxMixin = new com.vaadin.ui.ComboBox with ComboBoxMixin)
    extends AbstractSelect(p) with BlurNotifier with FocusNotifier {

  def inputPrompt: Option[String] = Option(p.getInputPrompt)
  def inputPrompt_=(inputPrompt: Option[String]) = p.setInputPrompt(inputPrompt.orNull)
  def inputPrompt_=(inputPrompt: String) = p.setInputPrompt(inputPrompt)

  def textInputAllowed = p.isTextInputAllowed
  def textInputAllowed_=(textInputAllowed: Boolean) = p.setTextInputAllowed(textInputAllowed)

  // from Select:

  def filteringMode = ComboBox.FilterinMode(p.getFilteringMode.ordinal)
  def filteringMode_=(filteringMode: ComboBox.FilterinMode.Value) = p.setFilteringMode(com.vaadin.shared.ui.combobox.FilteringMode.values.apply(filteringMode.id))

  def scrollToSelectedItem = p.isScrollToSelectedItem
  def scrollToSelectedItem_=(scrollToSelectedItem: Boolean) = p.setScrollToSelectedItem(scrollToSelectedItem)
}