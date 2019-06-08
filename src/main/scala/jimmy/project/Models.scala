package jimmy.project

object Models {

  case class User(id: Int, username: String)
  case class Users(users: Seq[User])

}
