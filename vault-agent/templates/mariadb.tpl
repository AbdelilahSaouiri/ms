{{- with secret "database/creds/mariadb-role" -}}
MARIADB_USER="{{ .Data.username }}"
MARIADB_PASSWORD="{{ .Data.password }}"
MARIADB_TTL="{{ .LeaseDuration }}"
{{- end }}
