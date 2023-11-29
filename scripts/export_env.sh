#!/usr/bin/env bash

# run with `source ./export_env.sh <env_file>`

ENV_FILE="$1"

while IFS= read -r line; do
    cleaned_line=$(echo "$line" | sed 's/\s*#.*//')
    if [[ -n "$cleaned_line" ]]; then
        export $cleaned_line
    fi
done < "$ENV_FILE"
