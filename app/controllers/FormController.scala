package controllers

import javax.inject.Inject

import models.Chanson
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import play.filters.csrf.CSRFConfig

import scala.collection.mutable.ArrayBuffer


class FormController @Inject()(val messagesApi: MessagesApi)(implicit csrfConfig: CSRFConfig) extends Controller with I18nSupport {

  private val chansons = ArrayBuffer(
    Chanson("Chanson 1", "Jul", "NikLa Polisse"),
    Chanson("Chanson 2", "oiu", "album")
  )


  def listChansons = Action { implicit request =>
    // Pass an unpopulated form to the template
    Ok(views.html.listChansons(chansons.toSeq, Application.createChansonForm))
  }

  // This will be the action that handles our form post
  def createChanson = Action { implicit request =>
    val formValidationResult = Application.createChansonForm.bindFromRequest
    formValidationResult.fold({ formWithErrors =>
      // This is the bad case, where the form had validation errors.
      // Let's show the user the form again, with the errors highlighted.
      // Note how we pass the form with errors to the template.
      BadRequest(views.html.listChansons(chansons.toSeq, formWithErrors))
    }, { chanson =>
      // This is the good case, where the form was successfully parsed as a Widget.
      chansons.append(chanson)
      Redirect(routes.FormController.listChansons)
    })
  }

}

object Application {

  /** The form definition for the "create a widget" form.
    *  It specifies the form fields and their types,
    *  as well as how to convert from a Widget to form data and vice versa.
    */
  val createChansonForm = Form(
    mapping(
      "titre" -> text,
      "artiste" -> text,
      "album" -> text
    )(Chanson.apply)(Chanson.unapply)

  )

}
