(function () {
    'use strict';
    angular
        .module('volunteercrowdApp')
        .factory('Need', Need);

    Need.$inject = ['$resource', 'DateUtils'];

    function Need($resource, DateUtils) {
        var resourceUrl = 'api/needs/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
                    data.modificationDate = DateUtils.convertDateTimeFromServer(data.modificationDate);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    }
})();

(function () {
    'use strict';
    angular
        .module('volunteercrowdApp')
        .factory('AppUserNeed', AppUserNeed);

    AppUserNeed.$inject = ['$resource', 'DateUtils'];

    function AppUserNeed($resource, DateUtils) {
        var resourceUrl = 'api/appUser/needs/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data,headers) {
                    var response = {};
                    data = angular.fromJson(data);
                    data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
                    data.modificationDate = DateUtils.convertDateTimeFromServer(data.modificationDate);

                    response.data = data;
                    response.headers = headers();
                    return response;
                }
            },
            'update': {method: 'PUT'}
        });
    }
})();
