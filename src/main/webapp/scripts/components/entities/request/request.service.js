'use strict';

angular.module('volunteercrowdApp')
    .factory('Request', function ($resource, DateUtils) {
        return $resource('api/requests/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creationDate = DateUtils.convertLocaleDateFromServer(data.creationDate);
                    data.finishDate = DateUtils.convertLocaleDateFromServer(data.finishDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.creationDate = DateUtils.convertLocaleDateToServer(data.creationDate);
                    data.finishDate = DateUtils.convertLocaleDateToServer(data.finishDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.creationDate = DateUtils.convertLocaleDateToServer(data.creationDate);
                    data.finishDate = DateUtils.convertLocaleDateToServer(data.finishDate);
                    return angular.toJson(data);
                }
            }
        });
    });
