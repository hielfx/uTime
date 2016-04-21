(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .factory('AdministratorSearch', AdministratorSearch);

    AdministratorSearch.$inject = ['$resource'];

    function AdministratorSearch($resource) {
        var resourceUrl =  'api/_search/administrators/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
