#include <bits/stdc++.h>
#include <ext/pb_ds/assoc_container.hpp>
#include <ext/pb_ds/tree_policy.hpp>

using namespace std;
using namespace __gnu_pbds;

#define INF 1<<30
#define INFLL 1LL<<61
#define EPS 1e-9
#define LSOne(S) ((S) & -(S))
#define ll long long
#define ld long double
#define vi vector<int>
#define vvi vector<vi>
#define vd vector<double>
#define vvd vector<vd>
#define vll vector<ll>
#define vvll vector<vll>
#define pii pair<int, int>
#define pdi pair<double, int>
#define pid pair<int, double>
#define pdd pair<double, double>
#define endl '\n'
#define pb push_back
#define debug(x) cout<<#x<<" -> "<<x<<'\n'
#define all(x) (x).begin(), (x).end()
#define rall(x) (x).rbegin(), (x).rend()
#define uni(x) (x).erase(unique(all(x)), (x).end())
#define rep(i, n) for (ll i = 0; i < (ll)(n); ++i)
#define rep1(i, n) for (ll i = 1; i <= (ll)(n); ++i)
#define umap unordered_map
#define uset unordered_set

int B, L, D, sum(0);
vi score;
vi total;
vi signup;
vi ship;
vvi books;

vi permutation;

ll solution = 0;
ll best_empty;
vi best_permutation;
vvi best_list;

void evaluate(vi perm) {
	set<int> scanned;
	ll cur_score = 0;
	ll cur_empty = 0;
	vvi cur_list;
	int days = 0;
	for(int i = 0; i < L; i++) {
		cur_list.pb(vi());
		int cur = perm[i];
		days += signup[cur];
		days = min(days, D);
		ll tot = (D - days + 1) * ship[cur];
		int k = 0;
		for(int j = 0; j < total[cur] && k < tot; j++) {
			if(scanned.find(books[cur][j]) == scanned.end()) {
				scanned.insert(books[cur][j]);
				cur_list[i].pb(books[cur][j]);
				cur_score += score[books[cur][j]];
				k++;
			}
		}
		if(k == 0) cur_empty++;
		if(days > D) break;
	}
	if (cur_score > solution) {
		solution = cur_score;
		best_empty = cur_empty;
		best_permutation = perm;
		best_list = cur_list;
	}
}

bool cmp_signup(int& a, int& b) {
	if(signup[a] == signup[b]) return total[a] > total[b];
	return signup[a] < signup[b];
}

void solve() {
	sort(permutation.begin(), permutation.end(), cmp_signup);
	evaluate(permutation);
}

bool cmp_score(int& a, int& b) {
	return score[a] > score[b];
}

int main() {
	ios::sync_with_stdio(false);
	cin.tie(NULL);
	cout.tie(NULL);
	
	cin>>B>>L>>D;
	permutation.assign(L, 0);
	iota(permutation.begin(), permutation.end(), 0);
	score.assign(B, 0);
	for(int i = 0; i < B; i++) {
		cin>>score[i];
		sum += score[i];
	}
	total.assign(L, 0);
	signup.assign(L, 0);
	ship.assign(L, 0);
	books.assign(L, vi());
	for(int i = 0; i < L; i++) {
		cin>>total[i]>>signup[i]>>ship[i];
		books[i].assign(total[i], 0);
		for(int j = 0; j < total[i]; j++) {
			cin>>books[i][j];
		}
		sort(books[i].begin(), books[i].end(), cmp_score);
	}
	solve();
	cerr<<solution<<"/"<<sum<<endl;
	
	int n = best_list.size();
	cout<<n - best_empty<<endl;
	for(int i = 0; i < n; i++) {
		if(best_list[i].size() == 0) continue;
		cout<<best_permutation[i]<<" "<<best_list[i].size()<<endl;
		for(auto j : best_list[i]) {
			cout<<j<<" ";
		}
		cout<<endl;
	}
	
	return 0;
}









