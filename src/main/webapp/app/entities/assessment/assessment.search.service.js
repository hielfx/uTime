(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .factory('AssessmentSearch', AssessmentSearch);

    AssessmentSearch.$inject = ['$resource'];

    function AssessmentSearch($resource) {
        var resourceUrl =  'api/_search/assessments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
