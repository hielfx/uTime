(function () {
    'use strict';
    angular
        .module('volunteercrowdApp')
        .factory('Assessment', Assessment);

    Assessment.$inject = ['$resource', 'DateUtils'];

    function Assessment($resource, DateUtils) {
        var resourceUrl = 'api/assessments/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creationMoment = DateUtils.convertDateTimeFromServer(data.creationMoment);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    }
})();
