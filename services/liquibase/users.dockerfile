ARG BUNDLER_IMAGE

FROM $BUNDLER_IMAGE as bundler

FROM 'liquibase/liquibase:4.24-alpine'
COPY --from=bundler '/app/services/liquibase/liquibase.users.properties' '/liquibase/liquibase.properties'
COPY --from=bundler '/app/services/liquibase/changelog' '/liquibase/changelog'
