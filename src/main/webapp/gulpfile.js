/**
 * Basic build system, use `gulp` to generate files in web/dist folder
 * use `gulp watch` when developing
 * gulp-include is used to include files, @see https://github.com/wiledal/gulp-include
 */

var gulp = require('gulp'),
    clean = require('gulp-clean'),
    concat = require('gulp-concat'),
    cssnano = require('gulp-cssnano'),
    htmlmin = require('gulp-htmlmin'),
    include = require('gulp-include'),
    jsEscape = require('gulp-js-escape'),
    size = require('gulp-size'),
    rename = require('gulp-rename'),
    runSequence = require('run-sequence'),
    sourcemaps = require('gulp-sourcemaps'),
    uglify = require('gulp-uglify'),
    wrapper = require('gulp-wrapper'),
    minifyCSS = require('gulp-csso'),
    concatCss = require('gulp-concat-css'),
    ngAnnotate = require('gulp-ng-annotate'),
    replace = require('gulp-replace-task'),
    argv = require('yargs').argv,
    html2string = require('gulp-html2string');


gulp.task('css', function () {
    return gulp.src(['node_modules/semantic-ui-css/semantic.css', 'style/*.css'])
        .pipe(concatCss("app.css"))
        .pipe(minifyCSS())
        .pipe(size({title: 'css'}))
        .pipe(gulp.dest('dist'));
});

gulp.task('font', function () {
    return gulp.src(['node_modules/semantic-ui-css/themes/default/assets/fonts/*'])
        .pipe(size({title: 'fonts'}))
        .pipe(gulp.dest('dist/themes/default/assets/fonts'));
});

gulp.task('js-libs', function () {
    return gulp.src([
        'node_modules/jquery/dist/jquery.min.js',
        'node_modules/semantic-ui-css/semantic.min.js',
        'node_modules/clipboard/dist/clipboard.min.js',
        'node_modules/angular/angular.min.js',
        'node_modules/angular-route/angular-route.min.js',
        'node_modules/ng-stomp/dist/ng-stomp.standalone.min.js',
        'node_modules/ngstorage/ngStorage.min.js'])
        .pipe(concat('libs.js'))
        .pipe(size({title: 'js-libs'}))
        .pipe(gulp.dest('dist'));
});

gulp.task('js-app', function () {
    var wsPort = argv.ws && argv.ws != 'false' ? argv.ws : '';
    var wssPort = argv.wss && argv.wss != 'false' ? argv.wss : '';
    return gulp.src([
        'app/**/*.js'

    ])
        .pipe(ngAnnotate())
        .pipe(concat('scrumPoker.js'))
        .pipe(replace({
            patterns: [
                {
                    match: 'inject_ws_port',
                    replacement: wsPort
                },
                {
                    match: 'inject_wss_port',
                    replacement: wssPort
                }
            ]
        }))
        .pipe(uglify())
        .pipe(size({title: 'js-app'}))
        .pipe(gulp.dest('dist'));
});

gulp.task('templates', function () {
    gulp.src('app/**/*.html')
        .pipe(html2string({
            createObj: true,
            objName: 'TEMPLATES'
        }))
        .pipe(rename({extname: '.js'}))
        .pipe(concat('templates.js'))
        .pipe(uglify())
        .pipe(size({title: 'html'}))
        .pipe(gulp.dest('dist'));
});

gulp.task('clean', function () {
    return gulp.src(['dist/*', 'tmp/*'])
        .pipe(clean());
});

gulp.task('build', function () {
    return runSequence('clean', 'css', 'font', 'templates', 'js-app', 'js-libs');
});

gulp.task('default', function () {
    return runSequence('build');
});