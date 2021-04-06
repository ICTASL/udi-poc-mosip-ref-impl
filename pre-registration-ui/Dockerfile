FROM node:11.15.0-alpine
RUN npm install
RUN npm run ng build --prod --base-href --aot=false

FROM nginx
COPY default.conf /etc/nginx/conf.d/
COPY dist/pre-registration-ui /usr/share/nginx/html
