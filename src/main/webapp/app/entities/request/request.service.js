(function () {
    'use strict';
    angular
        .module('volunteercrowdApp')
        .factory('Request', Request);

    Request.$inject = ['$resource', 'DateUtils'];

    function Request($resource, DateUtils) {
        var resourceUrl = 'api/requests/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
                    data.finishDate = DateUtils.convertDateTimeFromServer(data.finishDate);
                    data.modificationDate = DateUtils.convertDateTimeFromServer(data.modificationDate);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    }
})();
