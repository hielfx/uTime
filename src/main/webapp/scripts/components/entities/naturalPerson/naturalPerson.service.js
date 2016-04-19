'use strict';

angular.module('volunteercrowdApp')
    .factory('NaturalPerson', function ($resource, DateUtils) {
        return $resource('api/naturalPersons/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.birthDate = DateUtils.convertDateTimeFromServer(data.birthDate);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    });
