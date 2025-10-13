{{- with secret "kv/data/keycloak" -}}
KEYCLOAK_ADMIN="{{ .Data.data.KEYCLOAK_ADMIN }}"
KEYCLOAK_ADMIN_PASSWORD="{{ .Data.data.KEYCLOAK_ADMIN_PASSWORD }}"
{{- end }}
