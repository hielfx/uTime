'use strict';

angular.module('volunteercrowdApp')
    .controller('DisponibilityController', function ($scope, $state, Disponibility, DisponibilitySearch, ParseLinks) {

        $scope.disponibilitys = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Disponibility.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.disponibilitys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            DisponibilitySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.disponibilitys = result;
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
            $scope.disponibility = {
                startMoment: null,
                endMoment: null,
                id: null
            };
        };
    });
