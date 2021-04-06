FROM node:11.15.0-alpine
WORKDIR /home/ec2-user/preregapp
COPY package*.json /home/ec2-user/preregapp/
RUN npm install
COPY ./ /home/ec2-user/preregapp/
RUN npm run ng build --prod --base-href --aot=false

FROM nginx
COPY default.conf /etc/nginx/conf.d/
COPY dist/pre-registration-ui /usr/share/nginx/html
