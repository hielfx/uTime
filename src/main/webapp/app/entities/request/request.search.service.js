(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .factory('RequestSearch', RequestSearch);

    RequestSearch.$inject = ['$resource'];

    function RequestSearch($resource) {
        var resourceUrl =  'api/_search/requests/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
