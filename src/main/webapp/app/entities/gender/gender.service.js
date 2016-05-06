/**
 * Created by Daniel Sánchez on 04/05/2016.
 */

(function () {
    'use strict';
    angular
        .module('volunteercrowdApp')
        .factory('Gender', Gender);

    Gender.$inject = ['$resource'];

    function Gender($resource) {
        var resourceUrl = 'api/genders';
        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }/*,
             'update': { method:'PUT' }*/
        });
    }
})();
