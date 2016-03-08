'use strict';

angular.module('volunteercrowdApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


