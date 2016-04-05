'use strict';

angular.module('volunteercrowdApp')
    .controller('NeedController', function ($scope, $state, Need, NeedSearch, ParseLinks) {

        $scope.needs = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Need.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.needs = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            NeedSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.needs = result;
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
            $scope.need = {
                title: null,
                description: null,
                category: null,
                deleted: null,
                location: null,
                creationDate: null,
                modificationDate: null,
                id: null
            };
        };
    });
