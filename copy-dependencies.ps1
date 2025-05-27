# List of services
$services = @("api-gateway", "order-service", "logging-service", "email-service", "database-service")

# Copy shared dependencies to each service
foreach ($service in $services) {
    Write-Host "Copying dependencies to $service..."
    Copy-Item -Path "pom.xml" -Destination "$service\" -Force
    Copy-Item -Path "shared-dto" -Destination "$service\" -Recurse -Force
}

Write-Host "Dependencies copied successfully!" 