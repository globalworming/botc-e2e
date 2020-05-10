#!/usr/bin/env bash
mvn clean verify -P LOCAL_REST_API || true
mvn verify -P LOCAL_FRONTEND_WITH_MOCKED_INTEGRATIONS || true
mvn verify -P LOCAL_FRONTEND_INTEGRATED || true

google-chrome target/site/*/serenity-summary.html