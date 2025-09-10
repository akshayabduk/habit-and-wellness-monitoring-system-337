#!/bin/bash
cd /home/kavia/workspace/code-generation/habit-and-wellness-monitoring-system-337/personal_alerts_app
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

