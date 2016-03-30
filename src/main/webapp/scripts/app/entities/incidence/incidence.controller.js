'use strict';

angular.module('volunteercrowdApp')
    .controller('IncidenceController', function ($scope, $state, Incidence, IncidenceSearch, ParseLinks) {

        $scope.incidences = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Incidence.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.incidences = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            IncidenceSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.incidences = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.incidence = {
                creationDate: null,
                closed: null,
                description: null,
                adminComment: null,
                id: null
            };
        };
    });
