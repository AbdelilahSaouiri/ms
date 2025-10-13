vault {
  address = "http://vault:8200"
}

auto_auth {
  method "token" {
    config = {
      token_file = "/vault-agent/root_token"
    }
  }

  sink "file" {
    config = {
      path = "/home/vault/.vault-token"
    }
  }
}

cache {
  use_auto_auth_token = true
}

template {
  source      = "/templates/keycloak.tpl"
  destination = "/vault/secrets/keycloak.env"
  perms       = "0640"
}

# Optionnel : template pour credentials dynamiques DB
template {
  source      = "/templates/mariadb.tpl"
  destination = "/vault/secrets/mariadb.env"
  perms       = "0640"
}
