def route(language: String, code: String) = language match {
  case "ruby" | "mri" => Ruby(code)
}
