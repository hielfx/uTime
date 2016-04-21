(function() {
    'use strict';
    angular
        .module('volunteercrowdApp')
        .factory('Availability', Availability);

    Availability.$inject = ['$resource', 'DateUtils'];

    function Availability ($resource, DateUtils) {
        var resourceUrl =  'api/availabilities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.startMoment = DateUtils.convertDateTimeFromServer(data.startMoment);
                    data.endMoment = DateUtils.convertDateTimeFromServer(data.endMoment);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
