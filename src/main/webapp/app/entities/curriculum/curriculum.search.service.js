(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .factory('CurriculumSearch', CurriculumSearch);

    CurriculumSearch.$inject = ['$resource'];

    function CurriculumSearch($resource) {
        var resourceUrl =  'api/_search/curricula/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
