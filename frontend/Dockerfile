# Dockerfile for Angular
FROM node:18.20.5-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build 

# Production stage
FROM nginx:alpine
COPY --from=build /app/dist/to-do-frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 4200