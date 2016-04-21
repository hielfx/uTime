(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .factory('IncidenceSearch', IncidenceSearch);

    IncidenceSearch.$inject = ['$resource'];

    function IncidenceSearch($resource) {
        var resourceUrl =  'api/_search/incidences/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
